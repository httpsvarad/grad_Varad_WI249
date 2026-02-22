package io.github.httpsvarad.support.config;

import io.github.httpsvarad.support.handler.GlobalExceptionHandler;
import io.github.httpsvarad.support.properties.SupportProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(SupportProperties.class)
public class SupportAutoConfiguration {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(
            SupportProperties properties) {
        return new GlobalExceptionHandler(properties);
    }
}