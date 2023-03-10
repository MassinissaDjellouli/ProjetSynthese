package com.synthese.dto;

import com.synthese.enums.ProgramType;
import com.synthese.model.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramCreationDTO {
    private String name;
    private String description;
    private String type;

    public Program toModel(String establishmentId) {
        return Program.builder()
                .name(name)
                .description(description)
                .establishment(new ObjectId(establishmentId))
                .type(ProgramType.valueOf(type))
                .build();
    }
}