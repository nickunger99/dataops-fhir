package com.dataops.consumerFhir.domain.consumer;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.dataops.consumerFhir.domain.consumer.Observation.*;
import com.dataops.consumerFhir.domain.patient.PatientData;
import com.dataops.consumerFhir.domain.patient.PatientService;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Configuration
@EnableScheduling
public class ConsumerJob {
    private static final long ONE_MINUTE = 60 * 1000;
    private static final Logger logger = LoggerFactory.getLogger(ConsumerJob.class);

    @Autowired
    private IGenericClient fhirClient;

    @Autowired
    private PatientService patientService;

    @Autowired
    private TransactionTemplate transactionManager;

    @Scheduled(initialDelay = ONE_MINUTE, fixedDelay = ONE_MINUTE)
    public void execute() {
        logger.info("Checking patient data to send to fhir server...");
        boolean pending = true;

        while (pending) {
            pending = Boolean.TRUE.equals(transactionManager.execute(this::doInTransaction));
        }
    }

    private Boolean doInTransaction(TransactionStatus transactionStatus) {
        List<PatientData> patientDataList = patientService.findByPatientsFhirServerFalse();
        if (patientDataList.isEmpty()) {
            return false;
        }

        patientDataList.forEach(patientData -> {
            String[] partName = patientData.getName().split(" ", 2);
            String firstName = partName[0];
            String lastName = partName.length > 1 ? partName[1] : "";
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            Date date;
            try {
                date = formatDate.parse(patientData.getDateOfBirth());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Patient patient = patientService.createPatient(
                    firstName,
                    lastName,
                    patientData.getCpf(),
                    patientData.getGender(),
                    date,
                    patientData.getTel(),
                    patientData.getCountry()
            );
            // Salvar o paciente no servidor FHIR
            MethodOutcome outcome = fhirClient.create().resource(patient).execute();
            String patientId = outcome.getId().getIdPart();
            logger.info("Patient saved: {}, {}", patientData.getName(), patientId);
            Observation createObservation;
            // Criar uma Observação para o paciente
            if (!patientData.getObservation().isEmpty()) {

                switch (patientData.getObservation()) {
                    case "Gestante":
                        createObservation = PregnancyObservation.create(fhirClient, patientId);
                        break;
                    case "Diabético":
                        createObservation = DiabetesObservation.create(fhirClient, patientId);
                        break;
                    case "Hipertenso":
                        createObservation = HypertensionObservation.create(fhirClient, patientId);
                        break;
                    case "Gestante|Diabético":
                        createObservation = PregnancyAndDiabetesObservation.create(fhirClient, patientId);
                        break;
                    case "Diabético|Hipertenso":
                        createObservation = DiabetesAndHypertensionObservation.create(fhirClient, patientId);
                        break;
                    default:
                        throw new IllegalArgumentException("Condição desconhecida: " + patientData.getObservation());
                }
                fhirClient.create().resource(createObservation).execute();
                logger.info("Observation saved: {}", patientData.getName());
            }
            patientData.setFhirServer(true);
            patientService.save(patientData);
        });
        return true;
    }
}
