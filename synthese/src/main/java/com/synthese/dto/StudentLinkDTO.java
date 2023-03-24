package com.synthese.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentLinkDTO {
    @NotBlank
    @Size(min = 8, max = 12)
    private String username;
    @NotBlank
    private String programName;
}