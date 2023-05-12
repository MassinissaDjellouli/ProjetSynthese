package com.synthese.dto;

import com.synthese.enums.GenerationCycle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerationFailureInfoDTO {
    private GenerationCycle cycle;
    private int correctionCount;
    private String response;

}