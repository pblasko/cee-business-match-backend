package com.cee.business_match_backend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI businessMatchOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CEE Business Match Platform API")
                        .description("""
                                Backend API for a business matching platform where:
                                - Investors and Exporters can publish offers
                                - Investors, Exporters and Law Firms can interact through contact requests
                                - JWT authentication protects business endpoints
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Peter Blasko")
                                .email("blasko.peter.hu@gmail.com"))
                        .license(new License()
                                .name("Demo Project License")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .schemaRequirement(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                .externalDocs(new ExternalDocumentation()
                        .description("Project documentation")
                        .url("https://github.com/pblasko/cee-business-match-backend"));
    }

}
