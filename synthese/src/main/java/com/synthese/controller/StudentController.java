package com.synthese.controller;

import com.synthese.dto.ErrorDTO;
import com.synthese.dto.LoginDTO;
import com.synthese.enums.Errors;
import com.synthese.exceptions.StudentNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {

    StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok().body(studentService.login(loginDTO));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorDTO
                            .builder()
                            .error(Errors.INVALID_CREDENTIALS)
                            .build());
        } catch (StudentNotFoundException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorDTO
                            .builder()
                            .error(Errors.INVALID_CREDENTIALS)
                            .build());
        }
    }
}