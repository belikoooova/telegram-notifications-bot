package edu.java.scrapper.configuration;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApplicationConfig.class)
public class SchedulerConfig {
    private final ApplicationConfig applicationConfig;

    @Autowired
    public SchedulerConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public Duration schedulerInterval() {
        return applicationConfig.scheduler().interval();
    }
}
