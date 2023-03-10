package com.synthese.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TeacherDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String establishmentId;
    private List<CourseDTO> courses;
}