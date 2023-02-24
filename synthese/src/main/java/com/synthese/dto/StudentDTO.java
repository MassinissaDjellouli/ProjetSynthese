package com.synthese.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StudentDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private int currentSession;
    private String establishmentId;
}