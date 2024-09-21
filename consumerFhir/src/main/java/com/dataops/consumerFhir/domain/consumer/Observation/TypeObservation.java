package com.dataops.consumerFhir.domain.consumer.Observation;

public enum TypeObservation {
    PREGNANCY("Gestante"),
    DIABETES("Diabético"),
    HYPERTENSION("Hipertenso"),
    PREGNANCYDIABETES("Gestante|Diabético"),
    DIABETESHYPERTENSION("Diabético|Hipertenso");


    public final String label;

    TypeObservation(String label) {
        this.label = label;
    }
}
