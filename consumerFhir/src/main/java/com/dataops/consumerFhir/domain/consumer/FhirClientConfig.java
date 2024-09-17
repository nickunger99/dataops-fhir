package com.dataops.consumerFhir.domain.consumer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirClientConfig {
    @Value("${fhir.url}")
    private String fhirServerBase;
    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR4();
    }
    @Bean
    public IGenericClient fhirClient(FhirContext fhirContext) {
        return fhirContext.newRestfulGenericClient(fhirServerBase);
    }
}
