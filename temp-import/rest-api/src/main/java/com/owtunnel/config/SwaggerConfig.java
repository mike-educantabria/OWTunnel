package com.owtunnel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OWTunnel REST API")
                        .description("API documentation for OWTunnel VPN service.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("OWTunnel Support")
                                .email("support@owtunnel.com")
                                .url("https://owtunnel.com/help/support")
                        )
                        .license(new License()
                                .name("Proprietary License")
                                .url("https://owtunnel.com/legal/terms")
                        )
                );
    }
}
