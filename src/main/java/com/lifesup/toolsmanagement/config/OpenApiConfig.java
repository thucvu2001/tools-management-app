package com.lifesup.toolsmanagement.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Thuc Vu",
                        email = "vuthuc3152001@gmail.com"
                ),
                description = "OpenApi documentation for Tools Management",
                title = "OpenApi - Tools Management",
                version = "1.0",
                license = @License(
                        name = "License name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Term of service"
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8089/tools-management"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
