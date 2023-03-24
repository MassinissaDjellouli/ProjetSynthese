package com.synthese.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthese.dto.LoginDTO;
import com.synthese.dto.TeacherDTO;
import com.synthese.exceptions.TeacherNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.model.Teacher;
import com.synthese.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private TeacherController teacherController;
    @Mock
    private TeacherService teacherService;

    private LoginDTO loginDTO;
    private TeacherDTO teacherDTO;

    private JacksonTester<LoginDTO> loginDTOJacksonTester;
    private JacksonTester<Teacher> teacherJacksonTester;

    @BeforeEach
    public void setup() {
        loginDTO = LoginDTO.builder()
                .username("teacher123")
                .password("teacher123")
                .build();
        teacherDTO = TeacherDTO.builder()
                .id("5f9f1b9b9c9d1b2b8c1c1c1c")
                .username("teacher123")
                .firstName("teacher")
                .lastName("teacher")
                .build();

        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    public void loginTestHappyDay() throws Exception {
        when(teacherService.login(any())).thenReturn(List.of(teacherDTO));

        mockMvc.perform(post("/api/teacher/login")
                        .contentType("application/json")
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void loginTest404() throws Exception {
        when(teacherService.login(any())).thenThrow(UserNotFoundException.class);

        mockMvc.perform(post("/api/teacher/login")
                        .contentType("application/json")
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void loginTest422() throws Exception {
        when(teacherService.login(any())).thenThrow(TeacherNotFoundException.class);

        mockMvc.perform(post("/api/teacher/login")
                        .contentType("application/json")
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void loginTest401() throws Exception {
        when(teacherService.login(any())).thenThrow(WrongPasswordException.class);

        mockMvc.perform(post("/api/teacher/login")
                        .contentType("application/json")
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isUnauthorized());
    }


}