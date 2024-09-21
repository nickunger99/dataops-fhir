package com.dataops.consumerFhir.domain.consumer.observation;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.dataops.consumerFhir.domain.consumer.CreateObservation;
import org.hl7.fhir.r4.model.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DiabetesObservation implements ObservationResource{
    private static final Logger logger = LoggerFactory.getLogger(DiabetesObservation.class);

    public  Observation create(IGenericClient client, String patientId, String observation) {
        if (!Objects.equals(observation, TypeObservation.DIABETES.label)) {
            return null;
        }
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
