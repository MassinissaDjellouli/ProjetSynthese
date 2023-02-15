package com.synthese.service;

import com.synthese.model.Administrator;
import com.synthese.model.User;
import com.synthese.repository.AdministratorRepository;
import com.synthese.repository.UserRepository;
import com.synthese.security.Roles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AdministratorService {
    private AdministratorRepository adminRepository;
    private UserRepository userRepository;

    public void createDefaultAdministrator() {
        if (userRepository.findByUsername("admin").isPresent() || adminRepository.findByUsername("admin").isPresent()) {
            return;
        }
        adminRepository.save(Administrator.builder()
                .username("admin")
                .password("admin")
                .firstName("Massinissa")
                .lastName("Djellouli")
                .build());
        User defaultAdminUser = createNewUser("admin", "admin", Roles.ADMIN);
        defaultAdminUser.setExpirationDate(LocalDateTime.of(2033, 12, 31, 23, 59, 59));
        userRepository.save(defaultAdminUser);
    }

    private User createNewUser(String username, String password, Roles role) {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .expirationDate(LocalDateTime.now().plusMonths(6))
                .locked(false)
                .credentialsExpirationDate(LocalDateTime.now())
                .build();
    }
}