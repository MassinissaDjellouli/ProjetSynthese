package com.synthese.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStudentDTO {
    private String firstName;
    private String lastName;
    @Min(6)
    @Max(12)
    private String username;
    @Min(8)
    private String password;
    private String establishmentId;


}