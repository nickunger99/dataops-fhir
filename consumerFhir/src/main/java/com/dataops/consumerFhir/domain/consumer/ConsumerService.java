package com.dataops.consumerFhir.domain.consumer;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.dataops.consumerFhir.domain.consumer.Observation.*;
import com.dataops.consumerFhir.domain.patient.PatientData;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    private List<ObservationResource> observations;

    public void sendToFhir(PatientData patientData, IGenericClient fhirClient) {
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
        Patient patient = CreatePatient.createPatient(firstName, lastName, patientData.getCpf(), patientData.getGender(), date, patientData.getTel(), patientData.getCountry()
        );
        // Salvar o paciente no servidor FHIR
        MethodOutcome outcome = fhirClient.create().resource(patient).execute();
        String patientId = outcome.getId().getIdPart();
        logger.info("Patient saved: {}, {}", patientData.getName(), patientId);
        Observation createObservation;
        // Criar uma Observação para o paciente
        if (!patientData.getObservation().isEmpty()) {
            observations.forEach(observationResource -> observationResource.create(fhirClient, patientId, patientData.getObservation()));
        }
    }
}
