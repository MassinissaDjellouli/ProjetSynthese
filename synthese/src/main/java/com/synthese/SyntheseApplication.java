package com.synthese;

import com.synthese.security.CustomUserDetailsService;
import com.synthese.security.SpringSecurityConfig;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@SpringBootApplication
@AllArgsConstructor
public class SyntheseApplication {
    @Bean
    public DaoAuthenticationProvider authenticationProvider(@Lazy CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(SpringSecurityConfig.getPasswordEncoder());
        return authenticationProvider;
    }

    public static void main(String[] args) {
        SpringApplication.run(SyntheseApplication.class, args);
    }

}
