package com.synthese.service;

import com.synthese.dto.LoginDTO;
import com.synthese.enums.Roles;
import com.synthese.model.Teacher;
import com.synthese.model.User;
import com.synthese.repository.TeacherRepository;
import com.synthese.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @InjectMocks
    private TeacherService teacherService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TeacherRepository teacherRepository;

    private LoginDTO loginDTO;
    private User teacherUser;
    private Teacher teacher;

    @BeforeEach
    public void setup() {
        teacher = Teacher.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("teacher123")
                .establishment(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .firstName("teacher")
                .lastName("teacher")
                .build();

        teacherUser = User.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("teacher123")
                .password("teacher123")
                .role(Roles.TEACHER)
                .build();

        loginDTO = LoginDTO.builder()
                .username("teacher123")
                .password("teacher123")
                .build();
    }

    @Test
    public void loginTestHappyDay() throws Exception {
        when(userRepository.findByUsernameAndRole(anyString(), any())).thenReturn(Optional.of(teacherUser));
        when(teacherRepository.findByUserId(any())).thenReturn(List.of(teacher));

        teacherService.login(loginDTO);

        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(teacherRepository, times(1)).findByUserId(any());
    }

    @Test
    public void loginTestUserNotFound() {
        when(userRepository.findByUsernameAndRole(anyString(), any())).thenReturn(Optional.empty());

        try {
            teacherService.login(loginDTO);
        } catch (Exception e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(teacherRepository, times(0)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void loginTestWrongPassword() {
        teacherUser.setPassword("wrongPassword");
        when(userRepository.findByUsernameAndRole(anyString(), any())).thenReturn(Optional.of(teacherUser));

        try {
            teacherService.login(loginDTO);
        } catch (Exception e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(teacherRepository, times(0)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");

    }

    @Test
    public void loginTestTeacherNotFound() {
        when(userRepository.findByUsernameAndRole(anyString(), any())).thenReturn(Optional.of(teacherUser));
        when(teacherRepository.findByUserId(any())).thenReturn(new ArrayList<>());

        try {
            teacherService.login(loginDTO);
        } catch (Exception e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(teacherRepository, times(1)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");

    }
}