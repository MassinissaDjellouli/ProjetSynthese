package com.synthese.service;

import com.synthese.dto.LoginDTO;
import com.synthese.dto.ManagerDTO;
import com.synthese.enums.Roles;
import com.synthese.exceptions.ManagerNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.model.Manager;
import com.synthese.model.User;
import com.synthese.repository.ManagerRepository;
import com.synthese.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;

    public List<ManagerDTO> login(LoginDTO loginDTO) throws UserNotFoundException, WrongPasswordException, ManagerNotFoundException {
        Optional<User> user = userRepository.findByUsernameAndRole(loginDTO.getUsername(), Roles.MANAGER);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (!user.get().getPassword().equals(loginDTO.getPassword())) {
            throw new WrongPasswordException();
        }
        List<Manager> managerList = managerRepository.findByUserId(user.get().getId());
        if (managerList.isEmpty()) {
            throw new ManagerNotFoundException();
        }
        return managerList.stream().map(manager ->
                ManagerDTO.builder()
                        .id(user.get().getId().toString())
                        .firstName(manager.getFirstName())
                        .lastName(manager.getLastName())
                        .username(user.get().getUsername())
                        .build()
        ).toList();
    }
}