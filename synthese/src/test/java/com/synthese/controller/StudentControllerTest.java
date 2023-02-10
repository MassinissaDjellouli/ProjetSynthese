package com.synthese.controller;

import com.synthese.api.StudentController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
    MockMvc mockMvc;
    @InjectMocks
    StudentController studentController;
}
