package com.example.cityfeedback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CityFeedbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityFeedbackApplication.class, args);
        System.out.println("Hallo GitHub CI!");
    }
}

