package com.synthese.controller;

import com.synthese.dto.LoginDTO;
import com.synthese.dto.ManagerDTO;
import com.synthese.exceptions.ManagerNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.service.ManagerService;
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
@RequestMapping("/api/manager")
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping("/login")
    public ResponseEntity<ManagerDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok().body(managerService.login(loginDTO).toDTO());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ManagerNotFoundException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}