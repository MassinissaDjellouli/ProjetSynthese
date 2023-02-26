package com.synthese.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthese.dto.EstablishmentDTO;
import com.synthese.model.Establishment;
import com.synthese.service.SystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RootControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private RootController rootController;
    @Mock
    private SystemService systemService;

    EstablishmentDTO establishmentDTO;
    @BeforeEach
    public void setup() {
        establishmentDTO = EstablishmentDTO.builder()
                .id("5f9f1b9b9c9d1b2b8c1c1c1c")
                .name("establishment")
                .build();

        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(rootController).build();
    }

    @Test
    public void getEstablishmentsTestHappyDay() throws Exception {
        when(systemService.getEstablishments()).thenReturn(List.of(establishmentDTO));
        mockMvc.perform(get("/api/establishments"))
                .andExpect(status().isOk());
    }

}