package com.synthese.model;

import com.synthese.dto.CourseDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

@Document(collection = "courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CompoundIndex(name = "program_name", def = "{'program': 1, 'name': 1}", unique = true)
public class Course {

    @Id
    private ObjectId id;
    @NotBlank
    private String name;
    @Min(1)
    @Max(40)
    private int hoursPerWeek;
    @NotNull
    private ObjectId program;

    public CourseDTO toDTO() {
        return CourseDTO.builder()
                .id(id.toString())
                .name(name)
                .hoursPerWeek(hoursPerWeek)
                .program(program.toString())
                .build();
    }
}