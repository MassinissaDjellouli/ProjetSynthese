package com.synthese.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthese.dto.*;
import com.synthese.exceptions.*;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private StudentLinkDTO studentLinkDTO;
    private EstablishmentDTO establishmentDTO;
    private JacksonTester<LoginDTO> loginDTOJacksonTester;
    private JacksonTester<List<ObjectId>> teacherListJacksonTester;
    private JacksonTester<List<CourseCreationDTO>> courseCreationDTOJacksonTester;
    private JacksonTester<List<StudentLinkDTO>> studentLinkDTOJacksonTester;

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

        studentLinkDTO = StudentLinkDTO.builder()
                .username("student")
                .programName("program")
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
        doNothing().when(managerService).addTeacherToCourse(any(), any());
        mockMvc.perform(put("/api/manager/course/5f9f1b9b9c9d1b2b8c1c1c1c/addTeachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherListJacksonTester.write(List.of(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void addCourseListTestHappyDay() throws Exception {
        doNothing().when(managerService).addCourseList(any(), any());
        mockMvc.perform(post("/api/manager/program/5f9f1b9b9c9d1b2b8c1c1c1c/addCourseList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseCreationDTOJacksonTester.write(List.of(courseCreationDTO)).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void addCourseList400() throws Exception {
        doThrow(AlreadyExistingCourseException.class).when(managerService).addCourseList(any(), any());
        mockMvc.perform(post("/api/manager/program/5f9f1b9b9c9d1b2b8c1c1c1c/addCourseList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseCreationDTOJacksonTester.write(List.of(courseCreationDTO)).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addStudentListHappyDay() throws Exception {
        when(managerService.addStudentList(any(), any())).thenReturn(1);
        mockMvc.perform(put("/api/manager/establishment/5f9f1b9b9c9d1b2b8c1c1c1c/addStudentList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentLinkDTOJacksonTester.write(List.of(studentLinkDTO)).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void addStudentList400NoStudents() throws Exception {
        when(managerService.addStudentList(any(), any())).thenReturn(0);
        mockMvc.perform(put("/api/manager/establishment/5f9f1b9b9c9d1b2b8c1c1c1c/addStudentList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentLinkDTOJacksonTester.write(List.of(studentLinkDTO, studentLinkDTO)).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("NO_STUDENT_ADDED"));
    }

    @Test
    public void addStudentList400PartialStudents() throws Exception {
        when(managerService.addStudentList(any(), any())).thenReturn(1);
        mockMvc.perform(put("/api/manager/establishment/5f9f1b9b9c9d1b2b8c1c1c1c/addStudentList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentLinkDTOJacksonTester.write(List.of(studentLinkDTO, studentLinkDTO)).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("STUDENTS_PARTIALLY_ADDED"));
    }

    @Test
    public void getEstablishmentTestHappyDay() throws Exception {
        when(managerService.getEstablishment(any())).thenReturn(establishmentDTO);
        mockMvc.perform(get("/api/manager/establishment/5f9f1b9b9c9d1b2b8c1c1c1c"))
                .andExpect(status().isOk());
    }

    @Test
    public void getEstablishmentTest404() throws Exception {
        when(managerService.getEstablishment(any())).thenThrow(EstablishmentNotFoundException.class);
        mockMvc.perform(get("/api/manager/establishment/5f9f1b9b9c9d1b2b8c1c1c1c"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void generateScheduleTestHappyDay() throws Exception {
        doNothing().when(managerService).generateSchedules(any());
        mockMvc.perform(get("/api/manager/generateSchedules/5f9f1b9b9c9d1b2b8c1c1c1c"))
                .andExpect(status().isOk());
    }

    @Test
    public void generateScheduleTest400() throws Exception {
        doThrow(EstablishmentNotFoundException.class).when(managerService).generateSchedules(any());
        mockMvc.perform(get("/api/manager/generateSchedules/5f9f1b9b9c9d1b2b8c1c1c1c"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void generateScheduleTest500ChatGPT() throws Exception {
        doThrow(ChatGPTException.class).when(managerService).generateSchedules(any());
        mockMvc.perform(get("/api/manager/generateSchedules/5f9f1b9b9c9d1b2b8c1c1c1c"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("CHAT_GPT_FAILED"));
    }

    @Test
    public void generateScheduleTest500Schedule() throws Exception {
        doThrow(ScheduleGenerationException.class).when(managerService).generateSchedules(any());
        mockMvc.perform(get("/api/manager/generateSchedules/5f9f1b9b9c9d1b2b8c1c1c1c"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("SCHEDULE_GENERATION_FAILED"));
    }
}