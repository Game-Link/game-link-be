package com.gamelink.backend.global.config.swagger.jackson;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public interface JacksonFormatConfigurer {
    void configure(Jackson2ObjectMapperBuilder builder);
}
