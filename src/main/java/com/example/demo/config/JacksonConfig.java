package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        Hibernate6Module hibernateModule = new Hibernate6Module();
        // Do NOT force initialization of lazy associations during serialization
        hibernateModule.disable(Hibernate6Module.Feature.FORCE_LAZY_LOADING);
        // When lazy association is not loaded, serialize its identifier instead of failing or serializing proxy
        hibernateModule.enable(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);

        mapper.registerModule(hibernateModule);

        // Java Time module - handle java.time.* types (LocalDateTime, etc.)
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        mapper.registerModule(javaTimeModule);
        // Use ISO-8601 instead of timestamps for dates
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Avoid failure on empty beans (some proxies or objects may appear empty)
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        return mapper;
    }
}
