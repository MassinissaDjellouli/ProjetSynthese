package com.synthese.controller;

import com.synthese.dto.LoginDTO;
import com.synthese.dto.StudentDTO;
import com.synthese.exceptions.StudentNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.service.StudentService;
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
@RequestMapping("/api/student")
public class StudentController {

    StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<StudentDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok().body(studentService.login(loginDTO).toDTO());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (StudentNotFoundException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}