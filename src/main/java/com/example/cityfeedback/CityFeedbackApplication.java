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


//Testen des Users:
/*

{
  "email": "test@example.com",
  "password": "Passwort123",
  "role": "CITIZEN"
}

 */

