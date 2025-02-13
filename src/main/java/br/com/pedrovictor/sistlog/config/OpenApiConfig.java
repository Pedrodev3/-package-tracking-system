package br.com.pedrovictor.sistlog.config;

import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("packages")
                .pathsToMatch("/packages/**", "/api/v1/packages/**")
                .build();
    }

    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("SistLog - API de Rastreamento de Pacotes")
                        .version("1.0.0")
                        .description("Documentação da API do SistLog")
                        .contact(new Contact()
                                .name("Pedro Victor")
                                .email("pedrosaraivadev@gmail.com")
                                .url("https://github.com/Pedrodev3")
                        )
                );
    }
}
