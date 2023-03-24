package com.synthese.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthese.dto.CreateUserDTO;
import com.synthese.dto.EstablishmentDTO;
import com.synthese.dto.LoginDTO;
import com.synthese.dto.ProgramCreationDTO;
import com.synthese.exceptions.*;
import com.synthese.model.*;
import com.synthese.service.AdministratorService;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AdministratorControllerTest {
    MockMvc mockMvc;
    @InjectMocks
    AdministratorController administratorController;

    @Mock
    AdministratorService administratorService;

    JacksonTester<LoginDTO> loginDTOJacksonTester;

    JacksonTester<EstablishmentDTO> establishmentDTOJacksonTester;
    JacksonTester<CreateUserDTO> createUserDTOJacksonTester;
    JacksonTester<List<ProgramCreationDTO>> programCreationDTOJacksonTester;

    LoginDTO loginDTO;

    EstablishmentDTO establishmentDTO;

    Administrator administrator;
    Establishment establishment;
    Student student1;
    Student student2;
    Teacher teacher1;
    Teacher teacher2;
    Manager manager1;
    Manager manager2;

    CreateUserDTO createUserDTO;
    ProgramCreationDTO programCreationDTO;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        loginDTO = LoginDTO.builder()
                .username("admin123")
                .password("admin123")
                .build();

        administrator = Administrator.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("admin")
                .firstName("admin")
                .lastName("admin")
                .build();

        establishment = Establishment.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .build();

        establishmentDTO = EstablishmentDTO.builder()
                .id(establishment.getId().toString())
                .address("test")
                .name("test")
                .phone("1234567890")
                .classesStartTime("08:00")
                .openTime("07:00")
                .closeTime("18:00")
                .daysPerWeek(new ArrayList<>() {{
                    add("Lundi");
                    add("Mardi");
                    add("Mercredi");
                    add("Jeudi");
                    add("Vendredi");
                }})
                .dinnerLength("33")
                .periodLength(33)
                .periodsPerDay(3)
                .periodsBeforeDinner("1")
                .betweenPeriodsLength("33")
                .teachers(new ArrayList<>())
                .students(new ArrayList<>())
                .programs(new ArrayList<>())
                .build();

        student1 = Student.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .establishment(new ObjectId(establishmentDTO.getId()))
                .firstName("student1")
                .lastName("student1")
                .build();

        student2 = Student.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .establishment(new ObjectId(establishmentDTO.getId()))
                .firstName("student2")
                .lastName("student2")
                .build();

        teacher1 = Teacher.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .establishment(new ObjectId(establishmentDTO.getId()))
                .firstName("teacher1")
                .lastName("teacher1")
                .build();

        teacher2 = Teacher.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .establishment(new ObjectId(establishmentDTO.getId()))
                .firstName("teacher2")
                .lastName("teacher2")
                .build();

        manager1 = Manager.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .establishment(new ObjectId(establishmentDTO.getId()))
                .firstName("manager1")
                .lastName("manager1")
                .build();

        manager2 = Manager.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .establishment(new ObjectId(establishmentDTO.getId()))
                .firstName("manager2")
                .lastName("manager2")
                .build();
        createUserDTO = CreateUserDTO.builder()
                .establishmentId("5f9f1b9b9c9d1b2b8c1c1c1c")
                .firstName("name")
                .lastName("name")
                .password("password")
                .username("username")
                .build();

        programCreationDTO = ProgramCreationDTO.builder()
                .name("name")
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(administratorController).build();
    }

    @Test
    public void loginTestHappyDay() throws Exception {
        when(administratorService.login(loginDTO)).thenReturn(administrator);

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void loginTest404() throws Exception {
        when(administratorService.login(loginDTO)).thenThrow(UserNotFoundException.class);
        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void loginTest422() throws Exception {
        when(administratorService.login(loginDTO)).thenThrow(AdminNotFoundException.class);
        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void loginTest401() throws Exception {
        when(administratorService.login(loginDTO)).thenThrow(WrongPasswordException.class);
        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDTOJacksonTester.write(loginDTO).getJson()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void configureEstablishmentTestHappyDay() throws Exception {
        when(administratorService.configureEstablishment(any())).thenReturn(establishment.getId());
        mockMvc.perform(post("/api/admin/configureEstablishment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void configureEstablishmentTest400() throws Exception {
        when(administratorService.configureEstablishment(any())).thenThrow(NullPointerException.class);
        mockMvc.perform(post("/api/admin/configureEstablishment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getEstablishmentByAdminIdTestHappyDay() throws Exception {
        when(administratorService.getEstablishmentByAdminId(anyString())).thenReturn(List.of(establishment));
        mockMvc.perform(get("/api/admin/getEstablishmentByAdminId/{id}", "342342")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk());

    }

    @Test
    public void getEstablishmentByAdminIdTest404() throws Exception {
        when(administratorService.getEstablishmentByAdminId(anyString())).thenThrow(EstablishmentNotFoundException.class);
        mockMvc.perform(get("/api/admin/getEstablishmentByAdminId/{id}", "342342")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isNotFound());

    }

    @Test
    public void updateEstablishmentTestHappyDay() throws Exception {
        when(administratorService.updateEstablishment(any())).thenReturn(establishment.getId());
        mockMvc.perform(put("/api/admin/updateEstablishment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateEstablishmentTest400() throws Exception {
        when(administratorService.updateEstablishment(any())).thenThrow(NullPointerException.class);
        mockMvc.perform(put("/api/admin/updateEstablishment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createStudentTestHappyDay() throws Exception {
        when(administratorService.createStudent(any())).thenReturn(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"));
        mockMvc.perform(post("/api/admin/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserDTOJacksonTester.write(createUserDTO).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void createStudentTest400() throws Exception {
        when(administratorService.createStudent(any())).thenThrow(AlreadyExistingStudentException.class);
        mockMvc.perform(post("/api/admin/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserDTOJacksonTester.write(createUserDTO).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createManagerTestHappyDay() throws Exception {
        when(administratorService.createManager(any())).thenReturn(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"));
        mockMvc.perform(post("/api/admin/createManager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserDTOJacksonTester.write(createUserDTO).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void createManagerTest400() throws Exception {
        when(administratorService.createManager(any())).thenThrow(AlreadyExistingManagerException.class);
        mockMvc.perform(post("/api/admin/createManager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserDTOJacksonTester.write(createUserDTO).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createTeacherTestHappyDay() throws Exception {
        when(administratorService.createTeacher(any())).thenReturn(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"));
        mockMvc.perform(post("/api/admin/createTeacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserDTOJacksonTester.write(createUserDTO).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void createTeacherTest400() throws Exception {
        when(administratorService.createTeacher(any())).thenThrow(AlreadyExistingTeacherException.class);
        mockMvc.perform(post("/api/admin/createTeacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserDTOJacksonTester.write(createUserDTO).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getStudentByNameHappyDay() throws Exception {
        when(administratorService.getStudentsByName(anyString(), anyString())).thenReturn
                (List.of(student1.toDTO()));
        mockMvc.perform(get("/api/admin/getStudentsByName/{firstName}/{lastName}",
                        "student1", "student1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    public void getMultipleStudentsByNameHappyDay() throws Exception {
        student2.setFirstName("student1");
        student2.setLastName("student1");
        when(administratorService.getStudentsByName(anyString(), anyString())).thenReturn
                (List.of(student1.toDTO(), student2.toDTO()));
        mockMvc.perform(get("/api/admin/getStudentsByName/{firstName}/{lastName}/",
                        "student1", "student1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void getStudentByNameNoStudents() throws Exception {
        when(administratorService.getStudentsByName(anyString(), anyString()))
                .thenReturn(List.of());
        mockMvc.perform(get("/api/admin/getStudentsByName/{firstName}/{lastName}/",
                        "student1", "student1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    public void getTeacherByNameHappyDay() throws Exception {
        when(administratorService.getTeachersByName(anyString(), anyString())).thenReturn
                (List.of(teacher1.toDTO("")));
        mockMvc.perform(get("/api/admin/getTeachersByName/{firstName}/{lastName}",
                        "teacher1", "teacher1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    public void getMultipleTeachersByNameHappyDay() throws Exception {
        teacher2.setFirstName("teacher1");
        teacher2.setLastName("teacher1");
        when(administratorService.getTeachersByName(anyString(), anyString())).thenReturn
                (List.of(teacher1.toDTO(""), teacher2.toDTO("")));
        mockMvc.perform(get("/api/admin/getTeachersByName/{firstName}/{lastName}/",
                        "teacher1", "teacher1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void getTeacherByNameNoTeachers() throws Exception {
        when(administratorService.getTeachersByName(anyString(), anyString()))
                .thenReturn(List.of());
        mockMvc.perform(get("/api/admin/getTeachersByName/{firstName}/{lastName}/",
                        "teacher1", "teacher1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    public void getManagerByNameHappyDay() throws Exception {
        when(administratorService.getManagersByName(anyString(), anyString())).thenReturn
                (List.of(manager1.toDTO()));
        mockMvc.perform(get("/api/admin/getManagersByName/{firstName}/{lastName}",
                        "manager1", "manager1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    public void getMultipleManagersByNameHappyDay() throws Exception {
        manager2.setFirstName("manager1");
        manager2.setLastName("manager1");
        when(administratorService.getManagersByName(anyString(), anyString())).thenReturn
                (List.of(manager1.toDTO(), manager2.toDTO()));
        mockMvc.perform(get("/api/admin/getManagersByName/{firstName}/{lastName}/",
                        "manager1", "manager1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void getManagerByNameNoManagers() throws Exception {
        when(administratorService.getManagersByName(anyString(), anyString()))
                .thenReturn(List.of());
        mockMvc.perform(get("/api/admin/getManagersByName/{firstName}/{lastName}/",
                        "manager1", "manager1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(establishmentDTOJacksonTester.write(establishmentDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    public void addProgramListTestHappyDay() throws Exception {
        doNothing().when(administratorService).addProgramList(any(), any());
        mockMvc.perform(post("/api/admin/establishment/{establishmentId}/addProgramList", "55f5f5f5f5f5f5f5f5f5f5f5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(programCreationDTOJacksonTester.write(List.of(programCreationDTO)).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void addProgramListTest400() throws Exception {
        doThrow(AlreadyExistingProgramException.class).when(administratorService).addProgramList(any(), any());
        mockMvc.perform(post("/api/admin/establishment/{establishmentId}/addProgramList", "55f5f5f5f5f5f5f5f5f5f5f5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(programCreationDTOJacksonTester.write(List.of(programCreationDTO)).getJson()))
                .andExpect(status().isBadRequest());
    }
}