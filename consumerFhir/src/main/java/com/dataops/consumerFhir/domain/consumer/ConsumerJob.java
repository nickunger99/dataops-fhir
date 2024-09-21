package com.dataops.consumerFhir.domain.consumer;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.dataops.consumerFhir.domain.patient.PatientData;
import com.dataops.consumerFhir.domain.patient.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

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
    private ConsumerService consumerService;

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
            consumerService.sendToFhir(patientData, fhirClient);
            patientData.setFhirServer(true);
            patientService.save(patientData);
        });
        return true;
    }
}
