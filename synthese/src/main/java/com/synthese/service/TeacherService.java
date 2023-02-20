package com.synthese.service;

import com.synthese.dto.LoginDTO;
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

import java.util.Optional;

@Service
@AllArgsConstructor
public class TeacherService {
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

    public Teacher login(LoginDTO loginDTO) throws UserNotFoundException, WrongPasswordException, TeacherNotFoundException {
        Optional<User> user = userRepository.findByUsernameAndRole(loginDTO.getUsername(), Roles.TEACHER);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (!user.get().getPassword().equals(loginDTO.getPassword())) {
            throw new WrongPasswordException();
        }
        Optional<Teacher> teacherOpt = teacherRepository.findByUserId(user.get().getId());
        if (teacherOpt.isEmpty()) {
            throw new TeacherNotFoundException();
        }
        return teacherOpt.get();
    }
}