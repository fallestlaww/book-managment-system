package org.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for the Book Management System backend.
 * This class serves as the entry point for the application and enables
 * Spring Boot auto-configuration.
 * 
 * @author Book Management System
 * @version 1.0
 */
@SpringBootApplication
public class BackendApplication {

    /**
     * Main method that starts the Spring Boot application.
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
