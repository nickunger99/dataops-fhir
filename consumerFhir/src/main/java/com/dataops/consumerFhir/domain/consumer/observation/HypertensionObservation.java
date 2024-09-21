package com.dataops.consumerFhir.domain.consumer.observation;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.dataops.consumerFhir.domain.consumer.CreateObservation;
import org.hl7.fhir.r4.model.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class HypertensionObservation implements ObservationResource{
    private static final Logger logger = LoggerFactory.getLogger(HypertensionObservation.class);

    public Observation create(IGenericClient client, String patientId, String observation) {
        if (!Objects.equals(observation, TypeObservation.HYPERTENSION.label)) {
            return null;
        }
        // Pressão arterial sistólica
        Observation systolicPressureObservation = CreateObservation.createObservation(
                patientId,
                "8480-6", // Código LOINC para pressão arterial sistólica
                "Systolic Blood Pressure",
                160, // Valor da pressão sistólica (mmHg)
                "mmHg"
        );
        client.create().resource(systolicPressureObservation).execute();
        logger.info("Observation: {} Systolic Blood Pressure created!", patientId);

        // Pressão arterial diastólica
        Observation diastolicPressureObservation = CreateObservation.createObservation(
                patientId,
                "8462-4", // Código LOINC para pressão arterial diastólica
                "Diastolic Blood Pressure",
                100, // Valor da pressão diastólica (mmHg)
                "mmHg"
        );
        client.create().resource(diastolicPressureObservation).execute();
        logger.info("Observation: {} Diastolic Blood Pressure created!", patientId);
        return systolicPressureObservation;
    }
}
