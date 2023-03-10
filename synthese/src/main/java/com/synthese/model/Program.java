package com.synthese.model;

import com.synthese.dto.ProgramDTO;
import com.synthese.enums.ProgramType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "programs")
@CompoundIndex(name = "program_establishment", def = "{'establishment': 1, 'name': 1}", unique = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Program {
    @Id
    private ObjectId id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private ObjectId establishment;
    @NotNull
    private ProgramType type;

    public ProgramDTO toDTO() {
        return ProgramDTO.builder()
                .id(id.toString())
                .name(name)
                .description(description)
                .establishment(establishment.toString())
                .type(type.name())
                .build();
    }
}