package com.synthese.service;

import com.synthese.dto.EstablishmentDTO;
import com.synthese.dto.LoginDTO;
import com.synthese.enums.Roles;
import com.synthese.exceptions.AdminNotFoundException;
import com.synthese.exceptions.EstablishmentNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.model.Administrator;
import com.synthese.model.Establishment;
import com.synthese.model.User;
import com.synthese.repository.AdministratorRepository;
import com.synthese.repository.EstablishmentRepository;
import com.synthese.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdministratorService {
    //    private BCryptPasswordEncoder passwordEncoder;
    private final AdministratorRepository adminRepository;
    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final String DEFAULT_ADMIN_PASSWORD = "12345678";
    private final String DEFAULT_ADMIN_USERNAME = "admin123";


    public void createDefaultAdministrator() {
        if (userRepository.findByUsernameAndRole(DEFAULT_ADMIN_USERNAME, Roles.ADMIN).isPresent() || adminRepository.findByUsername(DEFAULT_ADMIN_USERNAME).isPresent()) {
            return;
        }
        User defaultAdminUser = createNewUser(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD, Roles.ADMIN);
        adminRepository.save(Administrator.builder()
                .username(DEFAULT_ADMIN_USERNAME)
                .firstName("Massinissa")
                .lastName("Djellouli")
                .userId(defaultAdminUser.getId())
                .build());
        defaultAdminUser.setExpirationDate(LocalDateTime.of(2033, 12, 31, 23, 59, 59));
    }

    private User createNewUser(String username, String password, Roles role) {
        return userRepository.save(User.builder()
                .username(username)
                .password(password)
                .role(role)
                .expirationDate(LocalDateTime.now().plusMonths(6))
                .locked(false)
                .credentialsExpirationDate(LocalDateTime.now())
                .build());
    }


    public Administrator login(LoginDTO loginDTO) throws UserNotFoundException, WrongPasswordException, AdminNotFoundException {
        Optional<User> user = userRepository.findByUsernameAndRole(loginDTO.getUsername(), Roles.ADMIN);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (!user.get().getPassword().equals(loginDTO.getPassword())) {
            throw new WrongPasswordException();
        }
        Optional<Administrator> adminOpt = adminRepository.findByUserId(user.get().getId());
        if (adminOpt.isEmpty()) {
            throw new AdminNotFoundException();
        }
        return adminOpt.get();
    }

    public ObjectId configureEstablishment(EstablishmentCreationDTO establishmentCreationDTO) {
        Establishment establishment = establishmentCreationDTO.toModel();
        establishment.setAdministrators(List.of(new ObjectId(establishmentCreationDTO.getAdminId())));
        return establishmentRepository.save(establishment).getId();
    }

    public ObjectId updateEstablishment(EstablishmentDTO establishmentOutDTO) {
        return establishmentRepository.save(establishmentOutDTO.toModel()).getId();
    }

    public List<Establishment> getEstablishmentByAdminId(String id) throws EstablishmentNotFoundException {
        List<Establishment> establishmentOptional = establishmentRepository.findByAdministratorsContaining(new ObjectId(id));
        if (establishmentOptional.isEmpty()) {
            throw new EstablishmentNotFoundException();
        }
        return establishmentOptional;
    }
}