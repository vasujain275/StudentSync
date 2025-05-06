package me.vasujain.studentsyncapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger("test-logger");
    }
}