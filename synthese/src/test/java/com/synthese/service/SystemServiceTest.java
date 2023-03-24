package com.synthese.service;

import com.synthese.dto.EstablishmentDTO;
import com.synthese.model.Establishment;
import com.synthese.repository.EstablishmentRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SystemServiceTest {
    @InjectMocks
    private SystemService systemService;
    @Mock
    private EstablishmentRepository establishmentRepository;


    private Establishment establishment;

    @BeforeEach
    public void setup() {
        establishment = Establishment.builder()
                .id(new ObjectId("5f9f1b9b9c9d1b2b8c1c1c1c"))
                .name("establishment")
                .build();
    }


    @Test
    public void getEstablishmentsHappyDay() throws Exception {
        when(establishmentRepository.findAll()).thenReturn(List.of(establishment));
        List<EstablishmentDTO> result = systemService.getEstablishments();
        verify(establishmentRepository, times(1)).findAll();
    }

}