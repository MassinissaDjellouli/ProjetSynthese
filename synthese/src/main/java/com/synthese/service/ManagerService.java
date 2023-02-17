package com.synthese.service;

import com.synthese.dto.LoginDTO;
import com.synthese.exceptions.ManagerNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.model.Manager;
import com.synthese.model.User;
import com.synthese.repository.ManagerRepository;
import com.synthese.repository.UserRepository;
import com.synthese.security.Roles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;

    public Manager login(LoginDTO loginDTO) throws UserNotFoundException, WrongPasswordException, ManagerNotFoundException {
        Optional<User> user = userRepository.findByUsernameAndRole(loginDTO.getUsername(), Roles.MANAGER);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (!user.get().getPassword().equals(loginDTO.getPassword())) {
            throw new WrongPasswordException();
        }
        Optional<Manager> managerOpt = managerRepository.getByUserId(user.get().getId());
        if (managerOpt.isEmpty()) {
            throw new ManagerNotFoundException();
        }
        return managerOpt.get();
    }
}