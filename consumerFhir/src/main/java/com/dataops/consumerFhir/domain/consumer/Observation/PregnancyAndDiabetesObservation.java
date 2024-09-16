package com.dataops.consumerFhir.domain.consumer.Observation;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.dataops.consumerFhir.domain.consumer.CreateObservation;
import org.hl7.fhir.r4.model.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PregnancyAndDiabetesObservation {
    private static final Logger logger = LoggerFactory.getLogger(PregnancyAndDiabetesObservation.class);

    public static Observation create(IGenericClient client, String patientId) {
        // Batimentos cardíacos fetais
        Observation fetalHeartRateObservation = CreateObservation.createObservation(
                patientId,
                "11884-4", // Código LOINC para batimentos cardíacos fetais
                "Fetal Heart Rate",
                140, // Valor da observação
                "bpm"
        );
        client.create().resource(fetalHeartRateObservation).execute();
        logger.info("Observation: {} Fetal Heart Rate created!", patientId);

        // Glicemia
        Observation glucoseObservation = CreateObservation.createObservation(
                patientId,
                "2339-0", // Código LOINC para glicose no sangue
                "Blood Glucose",
                90, // Valor da glicose (mg/dL)
                "mg/dL"
        );
        client.create().resource(glucoseObservation).execute();
        logger.info("Observation: {} Blood Glucose created!", patientId);
        return fetalHeartRateObservation;
    }
}
