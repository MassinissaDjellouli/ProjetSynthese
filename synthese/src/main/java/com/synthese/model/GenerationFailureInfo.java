package com.synthese.model;

import com.synthese.dto.GenerationFailureInfoDTO;
import com.synthese.enums.GenerationCycle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "generationFailureInfo")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerationFailureInfo {
    @Id
    private String generationID;
    private GenerationCycle cycle;
    private int correctionCount;
    private String response;

    public GenerationFailureInfoDTO toDTO() {
        return GenerationFailureInfoDTO.builder()
                .cycle(cycle)
                .correctionCount(correctionCount)
                .response(response)
                .build();
    }
}