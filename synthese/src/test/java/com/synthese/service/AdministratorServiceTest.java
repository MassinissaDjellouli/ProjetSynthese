package com.synthese.service;

import com.synthese.dto.CreateUserDTO;
import com.synthese.dto.EstablishmentCreationDTO;
import com.synthese.dto.EstablishmentDTO;
import com.synthese.dto.LoginDTO;
import com.synthese.enums.Roles;
import com.synthese.exceptions.*;
import com.synthese.model.Administrator;
import com.synthese.model.Establishment;
import com.synthese.model.Student;
import com.synthese.model.User;
import com.synthese.repository.AdministratorRepository;
import com.synthese.repository.EstablishmentRepository;
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

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
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
    private EstablishmentRepository establishmentRepository;
    private Administrator administrator;
    private User adminUser;
    private LoginDTO loginDTO;
    private User studentUser;
    private Student student1;
    private Student student2;
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
                .dinnerLength("33")
                .periodLength(33)
                .periodsPerDay(3)
                .periodsBeforeDinner("1")
                .betweenPeriodsLength("33")
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
                .dinnerLength("33")
                .periodLength(33)
                .periodsPerDay(3)
                .periodsBeforeDinner("1")
                .betweenPeriodsLength("33")
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

        createUserDTO = CreateUserDTO.builder()
                .firstName("student")
                .lastName("student")
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
}