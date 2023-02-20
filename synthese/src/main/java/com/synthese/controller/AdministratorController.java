package com.synthese.controller;

import com.synthese.dto.*;
import com.synthese.enums.Errors;
import com.synthese.exceptions.AdminNotFoundException;
import com.synthese.exceptions.EstablishmentNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.model.Establishment;
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
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok().body(adminService.login(loginDTO).toDTO());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO
                    .builder()
                    .error(Errors.ADMIN_NOT_FOUND)
                    .build());
        } catch (AdminNotFoundException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDTO
                    .builder()
                    .error(Errors.WRONG_PASSWORD)
                    .build());
        }
    }

    @PostMapping("/configureEstablishment")
    public ResponseEntity<?> configureEstablishment(@Valid @RequestBody EstablishmentCreationDTO establishmentInDTO) {
        try {
            String id = adminService.configureEstablishment(establishmentInDTO).toString();
            return ResponseEntity.ok().body(DataDTO.<String>builder().data(id).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO
                    .builder()
                    .error(Errors.INVALID_ESTABLISHMENT)
                    .build()
            );
        }
    }

    @GetMapping("/getEstablishmentByAdminId/{id}")
    public ResponseEntity<?> getEstablishmentByAdminId(@PathVariable String id) {
        try {
            return ResponseEntity.ok().body(adminService.getEstablishmentByAdminId(id)
                    .stream().map(Establishment::toDTO).toList()
            );
        } catch (EstablishmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO
                    .builder()
                    .error(Errors.ESTABLISHMENT_NOT_FOUND)
                    .build());
        }
    }

}