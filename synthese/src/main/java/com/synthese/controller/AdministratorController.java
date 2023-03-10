package com.synthese.controller;

import com.synthese.dto.*;
import com.synthese.enums.Errors;
import com.synthese.exceptions.*;
import com.synthese.model.Establishment;
import com.synthese.service.AdministratorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                    .error(Errors.INVALID_CREDENTIALS)
                    .build());
        } catch (AdminNotFoundException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDTO
                    .builder()
                    .error(Errors.INVALID_CREDENTIALS)
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

    @PutMapping("/updateEstablishment")
    public ResponseEntity<?> updateEstablishment(@Valid @RequestBody EstablishmentDTO establishmentDTO) {
        try {
            String id = adminService.updateEstablishment(establishmentDTO).toString();
            return ResponseEntity.ok().body(DataDTO.<String>builder().data(id).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO
                    .builder()
                    .error(Errors.INVALID_ESTABLISHMENT)
                    .build()
            );
        }
    }

    @PostMapping("/createStudent")
    public ResponseEntity<?> createStudent(@Valid @RequestBody CreateUserDTO creationDTO) {
        try {
            String id = adminService.createStudent(creationDTO).toString();
            return ResponseEntity.ok().body(DataDTO.<String>builder().data(id).build());
        } catch (AlreadyExistingStudentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO
                    .builder()
                    .error(Errors.ALREADY_EXISTING_STUDENT)
                    .build()
            );
        }
    }

    @PostMapping("/createManager")
    public ResponseEntity<?> createManager(@Valid @RequestBody CreateUserDTO creationDTO) {
        try {
            String id = adminService.createManager(creationDTO).toString();
            return ResponseEntity.ok().body(DataDTO.<String>builder().data(id).build());
        } catch (AlreadyExistingManagerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO
                    .builder()
                    .error(Errors.ALREADY_EXISTING_MANAGER)
                    .build()
            );
        }
    }

    @PostMapping("/createTeacher")
    public ResponseEntity<?> createTeacher(@Valid @RequestBody CreateUserDTO creationDTO) {
        try {
            String id = adminService.createTeacher(creationDTO).toString();
            return ResponseEntity.ok().body(DataDTO.<String>builder().data(id).build());
        } catch (AlreadyExistingTeacherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO
                    .builder()
                    .error(Errors.ALREADY_EXISTING_TEACHER)
                    .build()
            );
        }
    }

    @GetMapping("/getStudentsByName/{firstName}/{lastName}")
    public ResponseEntity<List<StudentDTO>> getStudentsByFirstAndLastName(
            @PathVariable String firstName, @PathVariable String lastName) {
        return ResponseEntity.ok().body(adminService.getStudentsByName(firstName, lastName));
    }


    @GetMapping("/getManagersByName/{firstName}/{lastName}")
    public ResponseEntity<List<ManagerDTO>> getManagersByFirstAndLastName(
            @PathVariable String firstName, @PathVariable String lastName) {
        return ResponseEntity.ok().body(adminService.getManagersByName(firstName, lastName));
    }

    @GetMapping("/getTeachersByName/{firstName}/{lastName}")
    public ResponseEntity<List<TeacherDTO>> getTeachersByFirstAndLastName(
            @PathVariable String firstName, @PathVariable String lastName) {
        return ResponseEntity.ok().body(adminService.getTeachersByName(firstName, lastName));
    }

    @PostMapping("/establishment/{id}/addProgramList")
    public ResponseEntity<?> addProgramList(@PathVariable String id, @RequestBody List<ProgramCreationDTO> programDTOList) {
        try {
            adminService.addProgramList(id, programDTOList);
            return ResponseEntity.ok().body(DataDTO.<String>builder().data("Success").build());
        } catch (AlreadyExistingProgramException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO
                    .builder()
                    .error(Errors.ALREADY_EXISTING_PROGRAM)
                    .build()
            );
        }
    }
}