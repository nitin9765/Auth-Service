package com.edigest.authservice;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DatabaseConfigLogger {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;


    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String kafkaServerProducer;

    @jakarta.annotation.PostConstruct
    public void logDatabaseConfig() {
        log.info("🔥 Database URL: {}", dbUrl);
        log.info("🔥 Database Username: {}", dbUser);
        log.info("🔥 Database Password: {}", dbPassword);
        log.info("🔥 kafka server: {}", kafkaServer);
        log.info("🔥 kafka consumer: {}", kafkaServerProducer);
    }
}
