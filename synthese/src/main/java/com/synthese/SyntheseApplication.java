package com.synthese;

import com.synthese.service.AdministratorService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(scanBasePackages = {"com.synthese", "com.synthese.controller", "com.synthese.dto", "com.synthese.model", "com.synthese.repository", "com.synthese.service"})
@AllArgsConstructor
public class SyntheseApplication implements CommandLineRunner {

    private final AdministratorService adminService;

//    @Bean
//    public BCryptPasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    public static void main(String[] args) {
        SpringApplication.run(SyntheseApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        adminService.createDefaultAdministrator();
    }
}