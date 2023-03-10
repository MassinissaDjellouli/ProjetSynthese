package com.synthese.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramDTO {
    private String id;
    private String name;
    private String description;
    private String establishment;
    private String type;
}