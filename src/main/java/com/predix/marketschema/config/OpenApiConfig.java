package com.predix.marketschema.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI predixOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("PrediX Market Schema API")
                        .description("Data contract and market lifecycle core service for PrediX prediction markets")
                        .version("1.0.0")
                        .contact(new Contact().name("PrediX").email("eng@predix.io")));
    }
}
