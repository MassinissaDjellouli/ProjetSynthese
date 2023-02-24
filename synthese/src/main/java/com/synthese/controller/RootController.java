package com.synthese.controller;

import com.synthese.dto.EstablishmentDTO;
import com.synthese.service.SystemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/")
@CrossOrigin
public class RootController {
    SystemService systemService;

    @GetMapping("/establishments")
    public ResponseEntity<List<EstablishmentDTO>> getEstablishments() {
        return ResponseEntity.ok().body(systemService.getEstablishments());
    }
}