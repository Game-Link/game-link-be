package com.gamelink.backend.global.config;

import com.gamelink.backend.global.auth.jwt.JwtProvider;
import com.gamelink.backend.global.config.jackson.JacksonDateTimeFormatter;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Map;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "[ GameLink ] API",
                version = SwaggerConfig.API_VERSION,
                description = "GameLink RESTFUL API 제공"
        ),
        servers = {
                @io.swagger.v3.oas.annotations.servers.Server(url = "/", description = "로컬 서버"),
        }
)
@SecurityScheme(
        name = JwtProvider.AUTHORIZATION,
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
    public static final String API_VERSION = "v1.0.0";

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            openApi.getComponents().getSchemas().forEach((s, schema) -> {
                Map<String, Schema> properties = schema.getProperties();
                if (properties == null) {
                    properties = Map.of();
                }
                for (String propertyName : properties.keySet()) {
                    Schema propertySchema = properties.get(propertyName);
                    if (propertySchema instanceof DateTimeSchema) {
                        String example = JacksonDateTimeFormatter.DATE_TIME_FORMAT.format(
                                LocalDateTime.of(2022, 1, 1, 10, 35, 17));
                        properties.replace(propertyName, new StringSchema()
                                .example(example)
                                .description(propertySchema.getDescription()));
                    }
                }
            });
        };
    }

    @Bean
    GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user-api")
                .pathsToMatch("/user/**")
                .build();
    }
}
