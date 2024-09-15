package com.dataops.api.infra;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DataOps API")
                        .description("Docs for DataOps API")
                        .version("BETA")
                        .termsOfService("")
                        .license(new License()
                                .name("")
                                .url("")
                        )
                ).externalDocs(
                        new ExternalDocumentation()
                                .description("Dev-Team")
                                .url(""));
    }
}
