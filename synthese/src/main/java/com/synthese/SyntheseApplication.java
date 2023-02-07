package com.synthese;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class SyntheseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyntheseApplication.class, args);
    }

}
