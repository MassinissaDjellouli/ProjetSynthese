package com.synthese.service;

import com.synthese.dto.LoginDTO;
import com.synthese.enums.Roles;
import com.synthese.model.Student;
import com.synthese.model.User;
import com.synthese.repository.StudentRepository;
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
public class StudentServiceTest {
    @InjectMocks
    private StudentService studentService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StudentRepository studentRepository;

    private Student student;
    private User studentUser;
    private LoginDTO loginDTO;

    @BeforeEach
    public void setup() {
        student = Student.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("student123")
                .firstName("student")
                .lastName("student")
                .build();

        studentUser = User.builder()
                .username("student123")
                .password("student123")
                .role(Roles.STUDENT)
                .build();

        loginDTO = LoginDTO.builder()
                .username("student123")
                .password("student123")
                .build();
    }


    @Test
    public void loginTestHappyDay() throws Exception {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(studentUser));
        when(studentRepository.findByUserId(any())).thenReturn(List.of(student));

        studentService.login(loginDTO);

        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(studentRepository, times(1)).findByUserId(any());
    }

    @Test
    public void loginTestUserNotFound() {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.empty());

        try {
            studentService.login(loginDTO);
        } catch (Exception e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(studentRepository, times(0)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");

    }

    @Test
    public void loginTestWrongPassword() {
        studentUser.setPassword("wrongPassword");
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(studentUser));

        try {
            studentService.login(loginDTO);
        } catch (Exception e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(studentRepository, times(0)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");

    }

    @Test
    public void loginTestStudentNotFound() {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(studentUser));
        when(studentRepository.findByUserId(any())).thenReturn(new ArrayList<>());

        try {
            studentService.login(loginDTO);
        } catch (Exception e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(studentRepository, times(1)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");

    }
}