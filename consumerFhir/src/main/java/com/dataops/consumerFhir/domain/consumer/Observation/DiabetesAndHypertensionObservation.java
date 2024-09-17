package com.dataops.consumerFhir.domain.consumer.Observation;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.dataops.consumerFhir.domain.consumer.CreateObservation;
import org.hl7.fhir.r4.model.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiabetesAndHypertensionObservation {
    private static final Logger logger = LoggerFactory.getLogger(DiabetesAndHypertensionObservation.class);

    public static Observation create(IGenericClient client, String patientId) {
        // Glicemia
        Observation glucoseObservation = CreateObservation.createObservation(
                patientId,
                "2339-0", // Código LOINC para glicose no sangue
                "Blood Glucose",
                180, // Valor da glicose (mg/dL)
                "mg/dL"
        );
        client.create().resource(glucoseObservation).execute();
        logger.info("Observation: {} Blood Glucose to Diabetic | Hypertension created!", patientId);

        // Pressão arterial sistólica
        Observation systolicPressureObservation = CreateObservation.createObservation(
                patientId,
                "8480-6", // Código LOINC para pressão arterial sistólica
                "Systolic Blood Pressure",
                170, // Valor da pressão sistólica (mmHg)
                "mmHg"
        );
        client.create().resource(systolicPressureObservation).execute();
        logger.info("Observation: {} Systolic Blood Pressure to Diabetic | Hypertension created!", patientId);

        // Pressão arterial diastólica
        Observation diastolicPressureObservation = CreateObservation.createObservation(
                patientId,
                "8462-4", // Código LOINC para pressão arterial diastólica
                "Diastolic Blood Pressure",
                110, // Valor da pressão diastólica (mmHg)
                "mmHg"
        );
        client.create().resource(diastolicPressureObservation).execute();
        logger.info("Observation: {} Diastolic Blood Pressure to Diabetic | Hypertension created!", patientId);
        return glucoseObservation;
    }
}
