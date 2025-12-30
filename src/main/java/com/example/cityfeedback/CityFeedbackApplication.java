package com.example.cityfeedback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CityFeedbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityFeedbackApplication.class, args);
        System.out.println("City Feedback gestartet");
    }
}


//Testen des Users:
/*

{
  "email": "test@example.com",
  "password": "Passwort123",
  "role": "CITIZEN"
}

{
  "email": "test2@example.com",
  "password": "Kennwort123",
  "role": "STAFF"
}

 */


//Testen des Feedbacks:
/*
        {
        "userId": "HIER USERID EINSETZEN, DIE IN DER DB ANGEZEIGT WIRD,
        "title": "Müll nicht abgeholt",
        "category": "UMWELT",
        "content": "Seit 3 Tagen steht der Müll auf der Straße."
        }

        {
        "userId": "896a32db-c327-45f2-9502-87e8f147583f",
        "title": "Straßenschild umgefallen",
        "category": "VERKEHR",
        "content": "Straßenschild ist umgefallen und blockiert den Weg."
        }

 */


