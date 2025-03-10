package com.edigest.authservice;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConfigLogger {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @jakarta.annotation.PostConstruct
    public void logDatabaseConfig() {
        System.out.println("🔥 Database URL: " + dbUrl);
        System.out.println("🔥 Database Username: " + dbUser);
        System.out.println("🔥 Database Password: " + dbPassword);
    }
}
