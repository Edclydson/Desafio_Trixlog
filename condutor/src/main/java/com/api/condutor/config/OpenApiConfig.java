package com.api.condutor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Condutores - Trixlog")
                        .version("v1.0.0")
                        .description("Microsserviço responsável pelo gerenciamento de condutores e seus veículos vinculados (posse).")
                        .contact(new Contact()
                                .name("Suporte Trixlog")
                                .email("suporte@trixlog.com")
                                .url("https://trixlog.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
