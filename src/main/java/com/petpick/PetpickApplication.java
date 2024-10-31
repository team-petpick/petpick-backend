package com.petpick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PetpickApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetpickApplication.class, args);
    }

}
