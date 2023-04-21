package com.synthese.service;

import com.synthese.dto.*;
import com.synthese.enums.Roles;
import com.synthese.exceptions.*;
import com.synthese.model.*;
import com.synthese.repository.*;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdministratorServiceTest {
    @InjectMocks
    private AdministratorService administratorService;

    @Mock
    private AdministratorRepository administratorRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private EstablishmentRepository establishmentRepository;
    @Mock
    private ProgramRepository programRepository;
    private Administrator administrator;
    private User adminUser;
    private LoginDTO loginDTO;
    private User studentUser;
    private User teacherUser;
    private User managerUser;
    private Student student1;
    private Student student2;
    private Teacher teacher1;
    private Teacher teacher2;
    private Manager manager1;
    private Manager manager2;
    private Program program1;
    private EstablishmentCreationDTO establishmentCreationDTO;
    private EstablishmentDTO establishmentDTO;
    private CreateUserDTO createUserDTO;

    @BeforeEach
    public void setup() {
        administrator = Administrator.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("admin123")
                .firstName("admin")
                .lastName("admin")
                .build();

        adminUser = User.builder()
                .username("admin123")
                .password("admin123")
                .role(Roles.ADMIN)
                .build();

        loginDTO = LoginDTO.builder()
                .username("admin123")
                .password("admin123")
                .build();

        establishmentCreationDTO = EstablishmentCreationDTO.builder()
                .adminId("5f9f1b9b9c9d1b2b8c1c1c1c")
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
                .dinnerLength(33)
                .periodLength(33)
                .periodsPerDay(3)
                .periodsBeforeDinner(1)
                .betweenPeriodsLength(33)
                .teachers(new ArrayList<>())
                .students(new ArrayList<>())
                .programs(new ArrayList<>())
                .build();

        establishmentDTO = EstablishmentDTO.builder()
                .id("5f9f1b9b9c9d1b2b8c1c1c1c")
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
                .dinnerLength(33)
                .periodLength(33)
                .periodsPerDay(3)
                .periodsBeforeDinner(1)
                .betweenPeriodsLength(33)
                .teachers(new ArrayList<>())
                .students(new ArrayList<>())
                .programs(new ArrayList<>())
                .build();

        studentUser = User.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("student123")
                .password("student123")
                .role(Roles.STUDENT)
                .build();

        teacherUser = User.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("teacher123")
                .password("teacher123")
                .role(Roles.TEACHER)
                .build();

        managerUser = User.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("manager123")
                .password("manager123")
                .role(Roles.MANAGER)
                .build();

        student1 = Student.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c54"))
                .userId(studentUser.getId())
                .establishment(new ObjectId("ffffffffffffffffffffffff"))
                .firstName("student")
                .lastName("student")
                .build();

        student2 = Student.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c53"))
                .userId(studentUser.getId())
                .establishment(new ObjectId(establishmentDTO.getId()))
                .firstName("student")
                .lastName("student")
                .build();

        teacher1 = Teacher.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c54"))
                .userId(teacherUser.getId())
                .establishment(new ObjectId("ffffffffffffffffffffffff"))
                .firstName("teacher")
                .lastName("teacher")
                .build();

        teacher2 = Teacher.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c53"))
                .userId(teacherUser.getId())
                .establishment(new ObjectId(establishmentDTO.getId()))
                .firstName("teacher")
                .lastName("teacher")
                .build();

        manager1 = Manager.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c54"))
                .userId(managerUser.getId())
                .establishment(new ObjectId("ffffffffffffffffffffffff"))
                .firstName("manager")
                .lastName("manager")
                .build();

        manager2 = Manager.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c53"))
                .userId(managerUser.getId())
                .establishment(new ObjectId(establishmentDTO.getId()))
                .firstName("manager")
                .lastName("manager")
                .build();

        createUserDTO = CreateUserDTO.builder()
                .firstName("student")
                .lastName("student")
                .build();

        program1 = Program.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c54"))
                .name("program1")
                .establishment(new ObjectId("ffffffffffffffffffffffff"))
                .build();
    }

    @Test
    public void createDefaultAdministratorTestHappyDay() {
        when(administratorRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.empty());
        when(administratorRepository.save(any())).thenReturn(administrator);
        when(userRepository.save(any())).thenReturn(adminUser);
        administratorService.createDefaultAdministrator();

        verify(administratorRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(administratorRepository, times(1)).save(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void createDefaultAdministratorTestAlreadyExisting() {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(adminUser));

        administratorService.createDefaultAdministrator();

        verify(administratorRepository, times(0)).findByUsername(anyString());
        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(administratorRepository, times(0)).save(any());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void loginTestHappyDay() throws Exception {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(adminUser));
        when(administratorRepository.findByUserId(any())).thenReturn(Optional.of(administrator));

        administratorService.login(loginDTO);

        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(administratorRepository, times(1)).findByUserId(any());
    }

    @Test
    public void loginTestUserNotFound() throws Exception {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.empty());

        try {
            administratorService.login(loginDTO);
        } catch (UserNotFoundException e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(administratorRepository, times(0)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void loginTestWrongPassword() throws Exception {
        adminUser.setPassword("wrongPassword");
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(adminUser));
        try {
            administratorService.login(loginDTO);
        } catch (WrongPasswordException e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(administratorRepository, times(0)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void loginTestAdminNotFound() throws Exception {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(adminUser));
        when(administratorRepository.findByUserId(any())).thenReturn(Optional.empty());

        try {
            administratorService.login(loginDTO);
        } catch (AdminNotFoundException e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(administratorRepository, times(1)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void configureEstablishmentHappyDay() throws Exception {
        when(establishmentRepository.save(any())).thenReturn(Establishment.builder().id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c")).build());
        administratorService.configureEstablishment(establishmentCreationDTO);

        verify(establishmentRepository, times(1)).save(any());
    }

    @Test
    public void getEstablishmentHappyDay() throws Exception {
        when(establishmentRepository.findByAdministratorsContaining(any())).thenReturn(List.of(Establishment.builder().build()));

        administratorService.getEstablishmentByAdminId(administrator.getId().toString());

        verify(establishmentRepository, times(1)).findByAdministratorsContaining(any());
    }

    @Test
    public void getEstablishmentNotFound() throws Exception {
        when(establishmentRepository.findByAdministratorsContaining(any())).thenReturn(new ArrayList<>());

        try {
            administratorService.getEstablishmentByAdminId(administrator.getId().toString());
        } catch (EstablishmentNotFoundException e) {
            verify(establishmentRepository, times(1)).findByAdministratorsContaining(any());
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void updateEstablishmentHappyDay() throws Exception {
        Establishment newEstablishment = Establishment.builder()
                .id(new ObjectId(establishmentDTO.getId()))
                .name("differentname")
                .build();
        when(establishmentRepository.save(any())).thenReturn(newEstablishment);

        administratorService.updateEstablishment(establishmentDTO);

        verify(establishmentRepository, times(1)).save(any());
        assertNotEquals(establishmentDTO.getName(), newEstablishment.getName());
    }

    @Test
    public void createStudentForNewUserHappyDay() throws Exception {
        createUserDTO.setUsername("student");
        createUserDTO.setPassword("student");
        createUserDTO.setEstablishmentId(establishmentDTO.getId());
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(studentUser);
        when(studentRepository.save(any())).thenReturn(student1);

        administratorService.createStudent(createUserDTO);

        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(userRepository, times(1)).save(any());
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    public void createStudentForExistingUserHappyDay() throws Exception {
        createUserDTO.setUsername(studentUser.getUsername());
        createUserDTO.setPassword(studentUser.getPassword());
        createUserDTO.setEstablishmentId(establishmentDTO.getId());
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(studentUser));
        when(studentRepository.save(any())).thenReturn(student2);

        administratorService.createStudent(createUserDTO);

        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(userRepository, times(0)).save(any());
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    public void createStudentForExistingUserAlreadyExists() {
        createUserDTO.setUsername(studentUser.getUsername());
        createUserDTO.setPassword(studentUser.getPassword());
        createUserDTO.setEstablishmentId(establishmentDTO.getId());
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(studentUser));
        when(studentRepository.findByUserIdAndEstablishment(any(), any())).thenReturn(Optional.of(student1));
        try {
            administratorService.createStudent(createUserDTO);
        } catch (AlreadyExistingStudentException e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(studentRepository, times(1)).findByUserIdAndEstablishment(any(), any());
            verify(userRepository, times(0)).save(any());
            verify(studentRepository, times(0)).save(any());
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void createTeacherForNewUserHappyDay() throws Exception {
        createUserDTO.setUsername("teacher");
        createUserDTO.setPassword("teacher");
        createUserDTO.setEstablishmentId(establishmentDTO.getId());
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(teacherUser);
        when(teacherRepository.save(any())).thenReturn(teacher1);

        administratorService.createTeacher(createUserDTO);

        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(userRepository, times(1)).save(any());
        verify(teacherRepository, times(1)).save(any());
    }

    @Test
    public void createTeacherForExistingUserHappyDay() throws Exception {
        createUserDTO.setUsername(teacherUser.getUsername());
        createUserDTO.setPassword(teacherUser.getPassword());
        createUserDTO.setEstablishmentId(establishmentDTO.getId());
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(teacherUser));
        when(teacherRepository.save(any())).thenReturn(teacher2);

        administratorService.createTeacher(createUserDTO);

        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(userRepository, times(0)).save(any());
        verify(teacherRepository, times(1)).save(any());
    }

    @Test
    public void createTeacherForExistingUserAlreadyExists() {
        createUserDTO.setUsername(teacherUser.getUsername());
        createUserDTO.setPassword(teacherUser.getPassword());
        createUserDTO.setEstablishmentId(establishmentDTO.getId());
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(teacherUser));
        when(teacherRepository.findByUserIdAndEstablishment(any(), any())).thenReturn(Optional.of(teacher1));
        try {
            administratorService.createTeacher(createUserDTO);
        } catch (AlreadyExistingTeacherException e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(teacherRepository, times(1)).findByUserIdAndEstablishment(any(), any());
            verify(userRepository, times(0)).save(any());
            verify(teacherRepository, times(0)).save(any());
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void createManagerForNewUserHappyDay() throws Exception {
        createUserDTO.setUsername("manager");
        createUserDTO.setPassword("manager");
        createUserDTO.setEstablishmentId(establishmentDTO.getId());
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(managerUser);
        when(managerRepository.save(any())).thenReturn(manager1);

        administratorService.createManager(createUserDTO);

        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(userRepository, times(1)).save(any());
        verify(managerRepository, times(1)).save(any());
    }

    @Test
    public void createManagerForExistingUserHappyDay() throws Exception {
        createUserDTO.setUsername(managerUser.getUsername());
        createUserDTO.setPassword(managerUser.getPassword());
        createUserDTO.setEstablishmentId(establishmentDTO.getId());
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(managerUser));
        when(managerRepository.save(any())).thenReturn(manager2);

        administratorService.createManager(createUserDTO);

        verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
        verify(userRepository, times(0)).save(any());
        verify(managerRepository, times(1)).save(any());
    }

    @Test
    public void createManagerForExistingUserAlreadyExists() {
        createUserDTO.setUsername(managerUser.getUsername());
        createUserDTO.setPassword(managerUser.getPassword());
        createUserDTO.setEstablishmentId(establishmentDTO.getId());
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(managerUser));
        when(managerRepository.findByUserIdAndEstablishment(any(), any())).thenReturn(Optional.of(manager1));
        try {
            administratorService.createManager(createUserDTO);
        } catch (AlreadyExistingManagerException e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(managerRepository, times(1)).findByUserIdAndEstablishment(any(), any());
            verify(userRepository, times(0)).save(any());
            verify(managerRepository, times(0)).save(any());
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void getStudentByNamesHappyDay() {
        when(studentRepository.findByFirstNameAndLastName(any(), any()))
                .thenReturn(List.of(student1));
        when(userRepository.findById(any())).thenReturn(Optional.of(studentUser));
        List<StudentDTO> studentDTO = administratorService.getStudentsByName(student1.getFirstName(), student1.getLastName());
        assertEquals(studentDTO.get(0).getFirstName(), student1.getFirstName());
        assertEquals(studentDTO.get(0).getLastName(), student1.getLastName());
        verify(studentRepository, times(1)).findByFirstNameAndLastName(any(), any());
        assertEquals(studentDTO.size(), 1);
    }

    @Test
    public void getStudentByNamesNotFound() {
        when(studentRepository.findByFirstNameAndLastName(any(), any()))
                .thenReturn(List.of());
        List<StudentDTO> studentDTO = administratorService.getStudentsByName(student1.getFirstName(), student1.getLastName());
        verify(studentRepository, times(1)).findByFirstNameAndLastName(any(), any());
        assertEquals(studentDTO.size(), 0);
    }

    @Test
    public void getStudentByNamesUserNotFound() {
        when(studentRepository.findByFirstNameAndLastName(any(), any()))
                .thenReturn(List.of(student1));
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        List<StudentDTO> studentDTO = administratorService.getStudentsByName(student1.getFirstName(), student1.getLastName());
        verify(studentRepository, times(1)).findByFirstNameAndLastName(any(), any());
        assertEquals(studentDTO.size(), 0);
    }

    @Test
    public void getTeacherByNamesHappyDay() {
        when(teacherRepository.findByFirstNameAndLastName(any(), any()))
                .thenReturn(List.of(teacher1));
        when(userRepository.findById(any())).thenReturn(Optional.of(teacherUser));
        List<TeacherDTO> teacherDTO = administratorService.getTeachersByName(teacher1.getFirstName(), teacher1.getLastName());
        assertEquals(teacherDTO.get(0).getFirstName(), teacher1.getFirstName());
        assertEquals(teacherDTO.get(0).getLastName(), teacher1.getLastName());
        verify(teacherRepository, times(1)).findByFirstNameAndLastName(any(), any());
        assertEquals(teacherDTO.size(), 1);
    }

    @Test
    public void getTeacherByNamesNotFound() {
        when(teacherRepository.findByFirstNameAndLastName(any(), any()))
                .thenReturn(List.of());
        List<TeacherDTO> teacherDTO = administratorService.getTeachersByName(teacher1.getFirstName(), teacher1.getLastName());
        verify(teacherRepository, times(1)).findByFirstNameAndLastName(any(), any());
        assertEquals(teacherDTO.size(), 0);
    }

    @Test
    public void getTeacherByNamesUserNotFound() {
        when(teacherRepository.findByFirstNameAndLastName(any(), any()))
                .thenReturn(List.of(teacher1));
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        List<TeacherDTO> teacherDTO = administratorService.getTeachersByName(teacher1.getFirstName(), teacher1.getLastName());
        verify(teacherRepository, times(1)).findByFirstNameAndLastName(any(), any());
        assertEquals(teacherDTO.size(), 0);
    }

    @Test
    public void getManagerByNamesHappyDay() {
        when(managerRepository.findByFirstNameAndLastName(any(), any()))
                .thenReturn(List.of(manager1));
        when(userRepository.findById(any())).thenReturn(Optional.of(managerUser));
        List<ManagerDTO> managerDTO = administratorService.getManagersByName(manager1.getFirstName(), manager1.getLastName());
        assertEquals(managerDTO.get(0).getFirstName(), manager1.getFirstName());
        assertEquals(managerDTO.get(0).getLastName(), manager1.getLastName());
        verify(managerRepository, times(1)).findByFirstNameAndLastName(any(), any());
        assertEquals(managerDTO.size(), 1);
    }

    @Test
    public void getManagerByNamesNotFound() {
        when(managerRepository.findByFirstNameAndLastName(any(), any()))
                .thenReturn(List.of());
        List<ManagerDTO> managerDTO = administratorService.getManagersByName(manager1.getFirstName(), manager1.getLastName());
        verify(managerRepository, times(1)).findByFirstNameAndLastName(any(), any());
        assertEquals(managerDTO.size(), 0);
    }

    @Test
    public void getManagerByNameUserNotFound() {
        when(managerRepository.findByFirstNameAndLastName(any(), any()))
                .thenReturn(List.of(manager1));
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        List<ManagerDTO> managerDTO = administratorService.getManagersByName(manager1.getFirstName(), manager1.getLastName());
        verify(managerRepository, times(1)).findByFirstNameAndLastName(any(), any());
        verify(userRepository, times(1)).findById(any());
        assertEquals(managerDTO.size(), 0);
    }

    @Test
    public void addProgramListTestHappyDay() throws Exception {
        when(programRepository.saveAll(any())).thenReturn(List.of(program1));
        administratorService.addProgramList(establishmentDTO.getId(), List.of(ProgramCreationDTO.builder()
                .name("Test")
                .description("Test")
                .type("Autre").build()));
        verify(programRepository, times(1)).saveAll(any());
    }

    @Test
    public void addProgramListTestAlreadyExists() {
        when(programRepository.saveAll(any())).thenThrow(DuplicateKeyException.class);

        try {
            administratorService.addProgramList(establishmentDTO.getId(), List.of(ProgramCreationDTO.builder()
                    .name("Test")
                    .description("Test")
                    .type("Autre").build()));
        } catch (AlreadyExistingProgramException e) {
            return;
        }
        fail("Exception not thrown");
    }
}