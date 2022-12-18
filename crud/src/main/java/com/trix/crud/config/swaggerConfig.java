package com.trix.crud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.Contact;


@Configuration
public class swaggerConfig {

    
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(informacoesDaApi())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.trix.crud.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo informacoesDaApi() {
        return new ApiInfoBuilder()
                .title("API Trixlog")
                .description("Api desenvolvida durante processo seletivo para a empresa Trixlog \n Projeto disponível em: https://github.com/Edclydson/Desafio_Trixlog")
                .contact(new Contact("Edclydson Sousa","https://linkedin.com/in/edclydson","edclydson.sousa@gmail.com"))
                .version("1.0.0")
                .build();
    }


}
