package com.synthese.service;

import com.synthese.dto.LoginDTO;
import com.synthese.dto.TeacherDTO;
import com.synthese.enums.Roles;
import com.synthese.exceptions.TeacherNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.model.Teacher;
import com.synthese.model.User;
import com.synthese.repository.TeacherRepository;
import com.synthese.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeacherService {
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

    public List<TeacherDTO> login(LoginDTO loginDTO) throws UserNotFoundException, WrongPasswordException, TeacherNotFoundException {
        Optional<User> user = userRepository.findByUsernameAndRole(loginDTO.getUsername(), Roles.TEACHER);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (!user.get().getPassword().equals(loginDTO.getPassword())) {
            throw new WrongPasswordException();
        }
        List<Teacher> teacherList = teacherRepository.findByUserId(user.get().getId());
        if (teacherList.isEmpty()) {
            throw new TeacherNotFoundException();
        }
        return teacherList.stream().map(teacher ->
                TeacherDTO.builder()
                        .id(user.get().getId().toString())
                        .firstName(teacher.getFirstName())
                        .lastName(teacher.getLastName())
                        .username(user.get().getUsername())
                        .establishmentId(teacher.getEstablishment().toString())
                        .build()
        ).toList();
    }
}