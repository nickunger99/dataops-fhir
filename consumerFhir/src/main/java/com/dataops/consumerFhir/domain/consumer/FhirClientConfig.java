package com.dataops.consumerFhir.domain.consumer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirClientConfig {

    private static final String FHIR_SERVER_BASE = "http://localhost:8080/fhir";  // URL do seu servidor FHIR

    @Bean
    public IGenericClient fhirClient() {
        // Cria um contexto FHIR para a versão R4
        FhirContext fhirContext = FhirContext.forR4();

        // Cria o cliente genérico apontando para o servidor FHIR
        return fhirContext.newRestfulGenericClient(FHIR_SERVER_BASE);
    }
}
