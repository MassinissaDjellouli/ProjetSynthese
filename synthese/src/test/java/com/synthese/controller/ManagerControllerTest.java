package com.synthese.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public class ManagerControllerTest {
    MockMvc mockMvc;
    @InjectMocks
    ManagerController managerController;
}