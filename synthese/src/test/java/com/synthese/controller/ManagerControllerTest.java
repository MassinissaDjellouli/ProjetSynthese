package com.synthese.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthese.dto.*;
import com.synthese.exceptions.ManagerNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.model.Course;
import com.synthese.model.Manager;
import com.synthese.service.ManagerService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ManagerControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private ManagerController managerController;
    @Mock
    private ManagerService managerService;
    private LoginDTO loginDTO;
    private ManagerDTO managerDTO;
    private CourseDTO courseDTO;
    private ProgramDTO programDTO;
    private TeacherDTO teacherDTO;
    private CourseCreationDTO courseCreationDTO;
    private JacksonTester<LoginDTO> loginDTOJacksonTester;
    private JacksonTester<List<ObjectId>> teacherListJacksonTester;
    private JacksonTester<List<CourseCreationDTO>> courseCreationDTOJacksonTester;
    @BeforeEach
    public void setup() {
        loginDTO = LoginDTO.builder()
                .username("manager123")
                .password("manager123")
                .build();
        managerDTO = ManagerDTO.builder()
                .id("5f9f1b9b9c9d1b2b8c1c1c1c")
                .username("manager123")
                .firstName("manager")
                .lastName("manager")
                .build();

        courseDTO = CourseDTO.builder()
                .id("5f9f1b9b9c9d1b2b8c1c1c1c")
                .name("course")
                .build();

programDTO = ProgramDTO.builder()
                .id("5f9f1b9b9c9d1b2b8c1c1c1c")
                .name("program")
                .build();

teacherDTO = TeacherDTO.builder()
                .id("5f9f1b9b9c9d1b2b8c1c1c1c")
                .firstName("teacher")
                .lastName("teacher")
                .build();
courseCreationDTO = CourseCreationDTO.builder()
                .name("course")
        .build();
        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
    }

    @Test
    public void loginTestHappyDay() throws Exception {
        when(managerService.login(any())).thenReturn(List.of(managerDTO));
        mockMvc.perform(post("/api/manager/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void loginTest404() throws Exception {
        when(managerService.login(any())).thenThrow(UserNotFoundException.class);
        mockMvc.perform(post("/api/manager/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void loginTest422() throws Exception {
        when(managerService.login(any())).thenThrow(ManagerNotFoundException.class);
        mockMvc.perform(post("/api/manager/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void loginTest401() throws Exception {
        when(managerService.login(any())).thenThrow(WrongPasswordException.class);
        mockMvc.perform(post("/api/manager/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getCoursesTestHappyDay() throws Exception {
        when(managerService.getCourses(any())).thenReturn(List.of(courseDTO));
        mockMvc.perform(get("/api/manager/program/5f9f1b9b9c9d1b2b8c1c1c1c/courses"))
                .andExpect(status().isOk());
    }

    @Test
    public void getProgramsTestHappyDay() throws Exception {
        when(managerService.getPrograms(any())).thenReturn(List.of(programDTO));
        mockMvc.perform(get("/api/manager/establishment/5f9f1b9b9c9d1b2b8c1c1c1c/programs"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTeachersTestHappyDay() throws Exception {
        when(managerService.getTeachers(any())).thenReturn(List.of(teacherDTO));
        mockMvc.perform(get("/api/manager/establishment/5f9f1b9b9c9d1b2b8c1c1c1c/teachers"))
                .andExpect(status().isOk());
    }

    @Test
    public void addTeacherTestHappyDay() throws Exception {
        doNothing().when(managerService).addTeacherToCourse(any(),any());
        mockMvc.perform(put("/api/manager/course/5f9f1b9b9c9d1b2b8c1c1c1c/addTeachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherListJacksonTester.write(List.of(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void addCourseListTestHappyDay() throws Exception {
        doNothing().when(managerService).addCourseList(any(),any());
        mockMvc.perform(post("/api/manager/program/5f9f1b9b9c9d1b2b8c1c1c1c/addCourseList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseCreationDTOJacksonTester.write(List.of(courseCreationDTO)).getJson()))
                .andExpect(status().isOk());
    }
}