package com.synthese.service;

import com.synthese.dto.*;
import com.synthese.enums.Roles;
import com.synthese.exceptions.*;
import com.synthese.model.*;
import com.synthese.repository.*;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdministratorService {
    //    private BCryptPasswordEncoder passwordEncoder;
    private final AdministratorRepository adminRepository;
    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final StudentRepository studentRepository;
    private final ManagerRepository managerRepository;
    private final TeacherRepository teacherRepository;
    private final String DEFAULT_ADMIN_PASSWORD = "12345678";
    private final String DEFAULT_ADMIN_USERNAME = "admin123";


    public void createDefaultAdministrator() {
        if (userRepository.findByUsernameAndRole(DEFAULT_ADMIN_USERNAME, Roles.ADMIN).isPresent() || adminRepository.findByUsername(DEFAULT_ADMIN_USERNAME).isPresent()) {
            return;
        }
        User defaultAdminUser = createNewUser(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD, Roles.ADMIN);
        adminRepository.save(Administrator.builder()
                .username(DEFAULT_ADMIN_USERNAME)
                .firstName("Massinissa")
                .lastName("Djellouli")
                .userId(defaultAdminUser.getId())
                .build());
        defaultAdminUser.setExpirationDate(LocalDateTime.of(2033, 12, 31, 23, 59, 59));
    }

    private User createNewUser(String username, String password, Roles role) {
        return userRepository.save(User.builder()
                .username(username)
                .password(password)
                .role(role)
                .expirationDate(LocalDateTime.now().plusMonths(6))
                .locked(false)
                .credentialsExpirationDate(LocalDateTime.now())
                .build());
    }


    public Administrator login(LoginDTO loginDTO) throws UserNotFoundException, WrongPasswordException, AdminNotFoundException {
        Optional<User> user = userRepository.findByUsernameAndRole(loginDTO.getUsername(), Roles.ADMIN);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (!user.get().getPassword().equals(loginDTO.getPassword())) {
            throw new WrongPasswordException();
        }
        Optional<Administrator> adminOpt = adminRepository.findByUserId(user.get().getId());
        if (adminOpt.isEmpty()) {
            throw new AdminNotFoundException();
        }
        return adminOpt.get();
    }

    public ObjectId configureEstablishment(EstablishmentCreationDTO establishmentCreationDTO) {
        Establishment establishment = establishmentCreationDTO.toModel();
        establishment.setAdministrators(List.of(new ObjectId(establishmentCreationDTO.getAdminId())));
        return establishmentRepository.save(establishment).getId();
    }

    public ObjectId updateEstablishment(EstablishmentDTO establishmentOutDTO) {
        return establishmentRepository.save(establishmentOutDTO.toModel()).getId();
    }

    public List<Establishment> getEstablishmentByAdminId(String id) throws EstablishmentNotFoundException {
        List<Establishment> establishmentOptional = establishmentRepository.findByAdministratorsContaining(new ObjectId(id));
        if (establishmentOptional.isEmpty()) {
            throw new EstablishmentNotFoundException();
        }
        return establishmentOptional;
    }

    public ObjectId createStudent(CreateUserDTO studentCreationDTO) throws AlreadyExistingStudentException {
        Optional<User> userOpt = userRepository.findByUsernameAndRole(studentCreationDTO.getUsername(), Roles.STUDENT);
        if (userOpt.isPresent()) {
            return createStudentForExistingUser(studentCreationDTO, userOpt.get());
        }
        User user = createNewUser(studentCreationDTO.getUsername(), studentCreationDTO.getPassword(), Roles.STUDENT);
        return createStudentForExistingUser(studentCreationDTO, user);
    }

    private ObjectId createStudentForExistingUser(CreateUserDTO studentCreationDTO, User user) throws AlreadyExistingStudentException {
        Optional<Student> studentOpt = studentRepository.findByUserIdAndEstablishment(
                user.getId(),
                new ObjectId(studentCreationDTO.getEstablishmentId()));
        if (studentOpt.isPresent()) {
            throw new AlreadyExistingStudentException();
        }
        return studentRepository.save(Student.builder()
                .userId(user.getId())
                .establishment(new ObjectId(studentCreationDTO.getEstablishmentId()))
                .firstName(studentCreationDTO.getFirstName())
                .lastName(studentCreationDTO.getLastName())
                .build()).getId();
    }

    public List<StudentDTO> getStudentsByName(String firstName, String lastName) {
        try{
            return studentRepository.findByFirstNameAndLastName(firstName, lastName).stream().map(student -> {
                User user = userRepository.findById(student.getUserId()).orElseThrow();
                return StudentDTO.builder()
                        .id(user.getId().toString())
                        .firstName(student.getFirstName())
                        .lastName(student.getLastName())
                        .username(user.getUsername())
                        .establishmentId(student.getEstablishment().toString())
                        .build();
            }).toList();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    public List<ManagerDTO> getManagersByName(String firstName, String lastName) {
        try{
            return managerRepository.findByFirstNameAndLastName(firstName, lastName).stream().map(manager -> {
                User user = userRepository.findById(manager.getUserId()).orElseThrow();
                return ManagerDTO.builder()
                        .id(user.getId().toString())
                        .firstName(manager.getFirstName())
                        .lastName(manager.getLastName())
                        .username(user.getUsername())
                        .establishmentId(manager.getEstablishment().toString())
                        .build();
            }).toList();
            }catch (Exception e){
                return new ArrayList<>();
            }
    }

    public List<TeacherDTO> getTeachersByName(String firstName, String lastName) {
        try {
            return teacherRepository.findByFirstNameAndLastName(firstName, lastName).stream().map(teacher -> {
                User user = userRepository.findById(teacher.getUserId()).orElseThrow();
                return TeacherDTO.builder()
                        .id(user.getId().toString())
                        .firstName(teacher.getFirstName())
                        .lastName(teacher.getLastName())
                        .username(user.getUsername())
                        .establishmentId(teacher.getEstablishment().toString())
                        .build();
            }).toList();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    public ObjectId createTeacher(CreateUserDTO creationDTO) throws AlreadyExistingTeacherException {
        Optional<User> userOpt = userRepository.findByUsernameAndRole(creationDTO.getUsername(), Roles.TEACHER);
        if (userOpt.isPresent()) {
            return createTeacherForExistingUser(creationDTO, userOpt.get());
        }
        User user = createNewUser(creationDTO.getUsername(), creationDTO.getPassword(), Roles.TEACHER);
        return createTeacherForExistingUser(creationDTO, user);

    }

    private ObjectId createTeacherForExistingUser(CreateUserDTO creationDTO, User user) throws AlreadyExistingTeacherException {

        Optional<Teacher> teacherOpt = teacherRepository.findByUserIdAndEstablishment(
                user.getId(),
                new ObjectId(creationDTO.getEstablishmentId()));
        if (teacherOpt.isPresent()) {
            throw new AlreadyExistingTeacherException();
        }
        return teacherRepository.save(Teacher.builder()
                .userId(user.getId())
                .establishment(new ObjectId(creationDTO.getEstablishmentId()))
                .firstName(creationDTO.getFirstName())
                .lastName(creationDTO.getLastName())
                .build()).getId();
    }

    public ObjectId createManager(CreateUserDTO creationDTO) throws AlreadyExistingManagerException {
        Optional<User> userOpt = userRepository.findByUsernameAndRole(creationDTO.getUsername(), Roles.MANAGER);
        if (userOpt.isPresent()) {
            return createManagerForExistingUser(creationDTO, userOpt.get());
        }
        User user = createNewUser(creationDTO.getUsername(), creationDTO.getPassword(), Roles.MANAGER);
        return createManagerForExistingUser(creationDTO, user);
    }

    private ObjectId createManagerForExistingUser(CreateUserDTO creationDTO, User user) throws AlreadyExistingManagerException {
        Optional<Manager> managerOpt = managerRepository.findByUserIdAndEstablishment(
                user.getId(),
                new ObjectId(creationDTO.getEstablishmentId()));
        if (managerOpt.isPresent()) {
            throw new AlreadyExistingManagerException();
        }
        return managerRepository.save(Manager.builder()
                .userId(user.getId())
                .establishment(new ObjectId(creationDTO.getEstablishmentId()))
                .firstName(creationDTO.getFirstName())
                .lastName(creationDTO.getLastName())
                .build()).getId();
    }
}