package com.synthese.controller;

import com.synthese.dto.LoginDTO;
import com.synthese.dto.TeacherDTO;
import com.synthese.exceptions.TeacherNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.service.TeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping("/login")
    public ResponseEntity<TeacherDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok().body(teacherService.login(loginDTO).toDTO());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (TeacherNotFoundException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}