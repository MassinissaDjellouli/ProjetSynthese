package com.synthese.service;

import com.synthese.dto.LoginDTO;
import com.synthese.enums.Roles;
import com.synthese.model.Manager;
import com.synthese.model.User;
import com.synthese.repository.ManagerRepository;
import com.synthese.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceTest {
    @InjectMocks
    private ManagerService managerService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ManagerRepository managerRepository;

    private Manager manager;
    private User managerUser;
    private LoginDTO loginDTO;

    @BeforeEach
    public void setup() {
        manager = Manager.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("manager123")
                .firstName("manager")
                .lastName("manager")
                .build();

        managerUser = User.builder()
                .username("manager123")
                .password("manager123")
                .role(Roles.MANAGER)
                .build();

        loginDTO = LoginDTO.builder()
                .username("manager123")
                .password("manager123")
                .build();
    }

    @Test
    public void loginTestHappyDay() throws Exception {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(managerUser));
        when(managerRepository.findByUserId(any())).thenReturn(Optional.of(manager));

        managerService.login(loginDTO);

        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(managerRepository, times(1)).findByUserId(any());
    }

    @Test
    public void loginTestUserNotFound() throws Exception {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.empty());

        try {
            managerService.login(loginDTO);
        } catch (Exception e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(managerRepository, times(0)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void loginTestWrongPassword() throws Exception {
        managerUser.setPassword("wrongPassword");
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(managerUser));

        try {
            managerService.login(loginDTO);
        } catch (Exception e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(managerRepository, times(0)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void loginTestManagerNotFound() throws Exception {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(managerUser));
        when(managerRepository.findByUserId(any())).thenReturn(Optional.empty());

        try {
            managerService.login(loginDTO);
        } catch (Exception e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(managerRepository, times(1)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");
    }
}