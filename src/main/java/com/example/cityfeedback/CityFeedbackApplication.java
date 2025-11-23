package com.example.cityfeedback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CityFeedbackApplication {

    private static final Logger logger = LoggerFactory.getLogger(CityFeedbackApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CityFeedbackApplication.class, args);
        logger.info("CityFeedback Application started successfully");
    }
}


