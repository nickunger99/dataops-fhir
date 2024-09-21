package com.dataops.consumerFhir.domain.consumer.observation;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Observation;

public interface ObservationResource {
    Observation create(IGenericClient client, String patientId, String observation);
}
