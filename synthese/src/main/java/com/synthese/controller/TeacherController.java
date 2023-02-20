package com.synthese.controller;

import com.synthese.dto.ErrorDTO;
import com.synthese.dto.LoginDTO;
import com.synthese.enums.Errors;
import com.synthese.exceptions.TeacherNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.service.TeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/teacher")
@CrossOrigin
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok().body(teacherService.login(loginDTO).toDTO());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO
                    .builder()
                    .error(Errors.TEACHER_NOT_FOUND)
                    .build());
        } catch (TeacherNotFoundException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDTO
                    .builder()
                    .error(Errors.WRONG_PASSWORD)
                    .build());
        }
    }
}