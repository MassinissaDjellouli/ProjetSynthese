package com.synthese.api;

import com.synthese.dto.StudentDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/student")
public class StudentController {
    @GetMapping("/login")
    public ResponseEntity<StudentDTO> login(@RequestBody StudentDTO studentDTO) {
        return null;
    }
}
