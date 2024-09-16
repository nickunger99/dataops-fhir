package com.dataops.consumerFhir.domain.consumer;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;

public class CreateObservation {
    public static Observation createObservation(String patientId, String loincCode, String display, double value, String unit) {
        Observation createObservation = new Observation();
        createObservation.setStatus(Observation.ObservationStatus.FINAL);

        // Código da observação
        createObservation.getCode().addCoding()
                .setSystem("http://loinc.org")
                .setCode(loincCode)
                .setDisplay(display);

        // Sujeito (paciente)
        createObservation.setSubject(new Reference("Patient/" + patientId));

        // Valor da observação
        Quantity valueQuantity = new Quantity();
        valueQuantity.setValue(value)
                .setUnit(unit)
                .setSystem("http://unitsofmeasure.org")
                .setCode(unit);
        createObservation.setValue(valueQuantity);

        return createObservation;

    }
}
