package com.synthese.service;

import com.synthese.dto.*;
import com.synthese.enums.ProgramType;
import com.synthese.enums.Roles;
import com.synthese.exceptions.AlreadyExistingCourseException;
import com.synthese.exceptions.EstablishmentNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private ProgramRepository programRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private EstablishmentRepository establishmentRepository;


    private Manager manager;
    private User managerUser;
    private LoginDTO loginDTO;
    private Course course;
    private Program program;
    private Establishment establishment;
    private Teacher teacher;
    private Student student;
    private User studentUser;
    private Establishment establishment2;

    @BeforeEach
    public void setup() {
        manager = Manager.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("manager123")
                .establishment(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .firstName("manager")
                .lastName("manager")
                .build();

        managerUser = User.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("manager123")
                .password("manager123")
                .role(Roles.MANAGER)
                .build();

        loginDTO = LoginDTO.builder()
                .username("manager123")
                .password("manager123")
                .build();

        course = Course.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .name("course")
                .program(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .build();

        program = Program.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .name("program")
                .establishment(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .type(ProgramType.Autre)
                .build();

        establishment = Establishment.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .name("establishment")
                .build();

        teacher = Teacher.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .firstName("teacher")
                .lastName("teacher")
                .username("teacher123")
                .establishment(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .courses(new ArrayList<>())
                .build();

        student = Student.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .firstName("student")
                .lastName("student")
                .establishment(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .build();

        studentUser = User.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .username("student123")
                .password("student123")
                .role(Roles.STUDENT)
                .build();
    }

    @Test
    public void loginTestHappyDay() throws Exception {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(managerUser));
        when(managerRepository.findByUserId(any())).thenReturn(List.of(manager));

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
        when(managerRepository.findByUserId(any())).thenReturn(new ArrayList<>());

        try {
            managerService.login(loginDTO);
        } catch (Exception e) {
            verify(userRepository, times(1)).findByUsernameAndRole(anyString(), any());
            verify(managerRepository, times(1)).findByUserId(any());
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void getCoursesTestHappyDay() throws Exception {
        when(courseRepository.findByProgram(any())).thenReturn(List.of(course));

        List<CourseDTO> courses = managerService.getCourses(program.getId().toString());

        assertEquals(1, courses.size());
        verify(courseRepository, times(1)).findByProgram(any());
    }

    @Test
    public void getCoursesNoCoursesFoundTest() throws Exception {
        when(courseRepository.findByProgram(any())).thenReturn(new ArrayList<>());
        List<CourseDTO> courses = managerService.getCourses(program.getId().toString());
        assertEquals(0, courses.size());
        verify(courseRepository, times(1)).findByProgram(any());
    }

    @Test
    public void getProgramsTestHappyDay() throws Exception {
        when(programRepository.findByEstablishment(any())).thenReturn(List.of(program));
        List<ProgramDTO> list = managerService.getPrograms(establishment.getId().toString());
        assertEquals(1, list.size());
        verify(programRepository, times(1)).findByEstablishment(any());
    }

    @Test
    public void getProgramsNoProgramsFoundTest() throws Exception {
        when(programRepository.findByEstablishment(any())).thenReturn(new ArrayList<>());
        List<ProgramDTO> list = managerService.getPrograms(establishment.getId().toString());
        assertEquals(0, list.size());
        verify(programRepository, times(1)).findByEstablishment(any());
    }

    @Test
    public void getTeachersTestHappyDay() throws Exception {
        when(teacherRepository.findByEstablishment(any())).thenReturn(List.of(teacher));
        List<TeacherDTO> list = managerService.getTeachers(establishment.getId().toString());
        assertEquals(1, list.size());
        verify(teacherRepository, times(1)).findByEstablishment(any());
    }

    @Test
    public void getTeachersNoTeachersFoundTest() throws Exception {
        when(teacherRepository.findByEstablishment(any())).thenReturn(new ArrayList<>());
        List<TeacherDTO> list = managerService.getTeachers(establishment.getId().toString());
        assertEquals(0, list.size());
        verify(teacherRepository, times(1)).findByEstablishment(any());
    }

    @Test
    public void getTeachersTestWithCourses() throws Exception {
        teacher.setCourses(List.of(course.getId()));
        when(teacherRepository.findByEstablishment(any())).thenReturn(List.of(teacher));
        List<TeacherDTO> list = managerService.getTeachers(establishment.getId().toString());
        assertEquals(1, list.size());
        verify(teacherRepository, times(1)).findByEstablishment(any());
    }

    @Test
    public void addCourseListTestHappyDay() throws Exception {
        when(programRepository.findById(any())).thenReturn(Optional.of(program));
        when(courseRepository.save(any())).thenReturn(course);

        managerService.addCourseList(program.getId().toString(), List.of(CourseCreationDTO.builder()
                .build()));

        verify(programRepository, times(1)).findById(any());
        verify(courseRepository, times(1)).save(any());
    }

    @Test
    public void addCourseListTestAlreadyExists() {
        when(programRepository.findById(any())).thenReturn(Optional.of(program));
        when(courseRepository.save(any())).thenThrow(DuplicateKeyException.class);

        try {
            managerService.addCourseList(program.getId().toString(), List.of(CourseCreationDTO.builder()
                    .build()));
        } catch (AlreadyExistingCourseException e) {
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void addTeacherToCourseTestHappyDay() throws Exception {
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));

        managerService.addTeacherToCourse(course.getId().toString(), List.of(teacher.getId()));
        assertEquals(course.getId().toString(), teacher.getCourses().get(0).toString());
        verify(teacherRepository, times(1)).findById(any());
    }

    @Test
    public void addStudentListTestHappyDay() throws Exception {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.of(studentUser));
        when(studentRepository.findByUserIdAndEstablishment(any(), any())).thenReturn(Optional.of(student));
        when(programRepository.findByEstablishmentAndName(any(), any())).thenReturn(Optional.of(program));

        int res = managerService.addStudentList(course.getId().toString(), List.of(StudentLinkDTO.builder()
                .programName(program.getName())
                .username(studentUser.getUsername())
                .build()));

        assertEquals(0, res);
    }

    @Test
    public void addStudentListTestNoStudentAdded() throws Exception {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.empty());

        int res = managerService.addStudentList(course.getId().toString(), List.of(StudentLinkDTO.builder()
                .programName(program.getName())
                .username(studentUser.getUsername())
                .build()));

        assertEquals(1, res);
    }

    @Test
    public void addStudentListTestStudentPartiallyAdded() throws Exception {
        when(userRepository.findByUsernameAndRole(any(), any())).thenReturn(Optional.empty(), Optional.of(studentUser));
        when(studentRepository.findByUserIdAndEstablishment(any(), any())).thenReturn(Optional.of(student));
        when(programRepository.findByEstablishmentAndName(any(), any())).thenReturn(Optional.of(program));

        int res = managerService.addStudentList(course.getId().toString(), List.of(StudentLinkDTO.builder().build(), StudentLinkDTO.builder()
                .programName(program.getName())
                .username(studentUser.getUsername())
                .build()));

        assertEquals(1, res);
    }

    @Test
    public void getEstablishmentTestHappyDay() throws Exception {
        when(establishmentRepository.findById(any())).thenReturn(Optional.of(establishment));
        EstablishmentDTO establishmentDTO = managerService.getEstablishment(establishment.getId().toString());
        assertEquals(establishment.getId().toString(), establishmentDTO.getId());
        verify(establishmentRepository, times(1)).findById(any());
    }

    @Test
    public void getEstablishmentTestNotFound() throws Exception {
        when(establishmentRepository.findById(any())).thenReturn(Optional.empty());
        try {
            managerService.getEstablishment(establishment.getId().toString());
        } catch (EstablishmentNotFoundException e) {
            return;
        }
        fail("Exception not thrown");
    }
}