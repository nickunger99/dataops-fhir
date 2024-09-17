package com.dataops.consumerFhir.domain.consumer.Observation;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.dataops.consumerFhir.domain.consumer.CreateObservation;
import org.hl7.fhir.r4.model.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiabetesObservation {
    private static final Logger logger = LoggerFactory.getLogger(DiabetesObservation.class);

    public static Observation create(IGenericClient client, String patientId) {
        Observation glucoseObservation = CreateObservation.createObservation(
                patientId,
                "2339-0", // Código LOINC para glicose no sangue
                "Blood Glucose",
                150, // Valor da glicose (mg/dL)
                "mg/dL"
        );
        client.create().resource(glucoseObservation).execute();
        logger.info("Patient ID: {} Observation de Blood Glucose to Diabetic created!", patientId);
        return glucoseObservation;
    }
}
