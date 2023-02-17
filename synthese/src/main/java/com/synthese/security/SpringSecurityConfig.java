//package com.synthese.security;
//
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
//@EnableWebSecurity
//@AllArgsConstructor
//public class SpringSecurityConfig {
//    private AuthenticationManager authenticationManager;
//    private DaoAuthenticationProvider authenticationProvider;
//    private CustomUserDetailsService userDetailsService;
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf().disable()
//                .httpBasic().disable()
//                .formLogin().loginPage("/api/student/login").permitAll().and()
//                .authenticationProvider(authenticationProvider)
//                .authenticationManager(authenticationManager)
//                .logout().disable()
//                .authorizeHttpRequests()
//                .requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
//                .requestMatchers("/api/student/login").anonymous()
//                .requestMatchers("/api/student/**").hasRole(Roles.STUDENT.name())
//                .requestMatchers("/api/teacher/**").hasRole(Roles.TEACHER.name())
//                .requestMatchers("/api/admin/**").hasRole(Roles.ADMIN.name())
//                .requestMatchers("/api/manager/**").hasRole(Roles.MANAGER.name())
//                .anyRequest().authenticated()
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
//        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//        return authenticationManagerBuilder.build();
//    }
//
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider(@Lazy CustomUserDetailsService userDetailsService) {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder);
//        return authenticationProvider;
//    }
//}