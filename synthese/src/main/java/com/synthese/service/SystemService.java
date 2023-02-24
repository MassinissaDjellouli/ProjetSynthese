package com.synthese.service;

import com.synthese.dto.EstablishmentDTO;
import com.synthese.repository.EstablishmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SystemService {

    private final EstablishmentRepository establishmentRepository;

    public List<EstablishmentDTO> getEstablishments() {
        return establishmentRepository.findAll().stream().map(establishment ->
                EstablishmentDTO.builder()
                        .id(establishment.getId().toString())
                        .name(establishment.getName())
                        .build()
        ).toList();
    }
}