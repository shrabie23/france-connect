package com.inetpsa.poc.fc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class is the Application Class.
 * 
 * @author Cristina
 */
@SpringBootApplication
public class Application {

    private static Class<Application> applicationClass = Application.class;

    /**
     * This is the most important method who calls the simulation.
     * 
     * @param args String[]
     */
    public static void main(String[] args) {
        SpringApplication.run(applicationClass, args);
    }

}
