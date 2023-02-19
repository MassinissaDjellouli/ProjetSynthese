package com.synthese.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ManagerDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
}