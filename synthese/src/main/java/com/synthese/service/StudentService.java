package com.synthese.service;

import com.synthese.dto.LoginDTO;
import com.synthese.dto.StudentDTO;
import com.synthese.enums.Roles;
import com.synthese.exceptions.StudentNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.model.Student;
import com.synthese.model.User;
import com.synthese.repository.StudentRepository;
import com.synthese.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public List<StudentDTO> login(LoginDTO loginDTO) throws UserNotFoundException, StudentNotFoundException, WrongPasswordException {
        Optional<User> user = userRepository.findByUsernameAndRole(loginDTO.getUsername(), Roles.STUDENT);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (!user.get().getPassword().equals(loginDTO.getPassword())) {
            throw new WrongPasswordException();
        }
        List<Student> studentList = studentRepository.findByUserId(user.get().getId());
        if (studentList.isEmpty()) {
            throw new StudentNotFoundException();
        }
        return studentList.stream().map(student ->
                StudentDTO.builder()
                        .id(user.get().getId().toString())
                        .firstName(student.getFirstName())
                        .lastName(student.getLastName())
                        .username(user.get().getUsername())
                        .establishmentId(student.getEstablishment().toString())
                        .build()
        ).toList();
    }
}