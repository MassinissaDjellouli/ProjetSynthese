package com.synthese.controller;

import com.synthese.dto.*;
import com.synthese.enums.Errors;
import com.synthese.exceptions.*;
import com.synthese.service.ManagerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/manager")
@CrossOrigin
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok().body(managerService.login(loginDTO));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO
                    .builder()
                    .error(Errors.INVALID_CREDENTIALS)
                    .build());
        } catch (ManagerNotFoundException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDTO
                    .builder()
                    .error(Errors.INVALID_CREDENTIALS)
                    .build());
        }
    }

    @GetMapping("/program/{programId}/courses")
    public ResponseEntity<?> getCourses(@PathVariable String programId) {
        return ResponseEntity.ok().body(managerService.getCourses(programId));
    }

    @GetMapping("/establishment/{establishmentId}/programs")
    public ResponseEntity<?> getPrograms(@PathVariable String establishmentId) {
        return ResponseEntity.ok().body(managerService.getPrograms(establishmentId));
    }

    @GetMapping("/establishment/{establishmentId}/teachers")
    public ResponseEntity<?> getTeachers(@PathVariable String establishmentId) {
        return ResponseEntity.ok().body(managerService.getTeachers(establishmentId));
    }

    @PutMapping("/course/{id}/addTeachers")
    public ResponseEntity<?> addCourses(@PathVariable String id, @RequestBody List<ObjectId> teachers) {
        managerService.addTeacherToCourse(id, teachers);
        return ResponseEntity.ok().body(DataDTO.<String>builder().data("Success").build());
    }

    @PostMapping("/program/{id}/addCourseList")
    public ResponseEntity<?> addCourseList(@PathVariable String id, @RequestBody List<CourseCreationDTO> courseCreationDTOList) {
        try {
            managerService.addCourseList(id, courseCreationDTOList);
            return ResponseEntity.ok().body(DataDTO.<String>builder().data("Success").build());
        } catch (AlreadyExistingCourseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO
                    .builder()
                    .error(Errors.ALREADY_EXISTING_COURSE)
                    .build()
            );
        }
    }

    @PutMapping("/establishment/{id}/addStudentList")
    public ResponseEntity<?> addStudentList(@PathVariable String id, @RequestBody List<StudentLinkDTO> studentList) {
        int addedStudentCount = managerService.addStudentList(id, studentList);
        if (addedStudentCount == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO
                    .builder()
                    .error(Errors.NO_STUDENT_ADDED)
                    .build());

        if (addedStudentCount == studentList.size())
            return ResponseEntity.ok().body(DataDTO.<String>builder().data("Success").build());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO
                .builder()
                .error(Errors.STUDENTS_PARTIALLY_ADDED)
                .build());
    }

    @GetMapping("/establishment/{id}")
    public ResponseEntity<?> getEstablishment(@PathVariable String id) {
        try {
            return ResponseEntity.ok().body(managerService.getEstablishment(id));
        } catch (EstablishmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDTO
                    .builder()
                    .error(Errors.ESTABLISHMENT_NOT_FOUND)
                    .build());
        }
    }

    @GetMapping("/generateSchedules/{programId}")
    public ResponseEntity<?> generateSchedules(@PathVariable String programId) {
        try {
            managerService.generateSchedules(programId);
            return ResponseEntity.ok().body(DataDTO.<String>builder().data("Success").build());
        } catch (EstablishmentNotFoundException | ProgramNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO
                    .builder()
                    .error(Errors.PROGRAM_NOT_FOUND)
                    .build());
        } catch (ScheduleGenerationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDTO
                    .builder()
                    .error(Errors.SCHEDULE_GENERATION_FAILED)
                    .build());
        } catch (ChatGPTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDTO
                    .builder()
                    .error(Errors.CHAT_GPT_FAILED)
                    .build());
        }
    }
}