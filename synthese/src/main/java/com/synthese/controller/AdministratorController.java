package com.synthese.controller;

import com.synthese.dto.AdminstratorDTO;
import com.synthese.dto.EstablishmentDTO;
import com.synthese.dto.LoginDTO;
import com.synthese.exceptions.AdminNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.service.AdministratorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin
public class AdministratorController {
    AdministratorService adminService;

    @PostMapping("/login")
    public ResponseEntity<AdminstratorDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok().body(adminService.login(loginDTO).toDTO());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AdminNotFoundException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/configureEstablishment")
    public ResponseEntity<EstablishmentDTO> configureEstablishment(@Valid @RequestBody EstablishmentDTO establishmentDTO) {
        try {
            adminService.configureEstablishment(establishmentDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}