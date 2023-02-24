package com.synthese;

import com.synthese.service.AdministratorService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication()
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