package com.synthese.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDTO {
    @Size(min = 6, max = 12)
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 8)
    private String password;

}