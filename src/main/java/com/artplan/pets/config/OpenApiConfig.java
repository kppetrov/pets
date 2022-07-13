package com.artplan.pets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    
    @Bean
    protected OpenAPI customCofiguration() {
        Contact contact = new Contact()
                .name("Konstantin Petrov")
                .email("k.p.petrov@gmail.com")
                .url("https://github.com/kppetrov");
        
        Info info = new Info()
                .title("Pets API")
                .version("1.0")
                .description("Test assignment for the position of Java developer")
                .contact(contact);
        
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}

