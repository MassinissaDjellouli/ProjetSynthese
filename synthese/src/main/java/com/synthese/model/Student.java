package com.synthese.model;

import com.synthese.dto.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "students")
@CompoundIndex(name = "student_index", def = "{'userId': 1, 'establishment': 1}", unique = true)
@AllArgsConstructor
@Builder
@Data
public class Student {
    @Id
    private ObjectId id;
    private ObjectId establishment;
    private ObjectId userId;
    private String firstName;
    private String lastName;
    private int currentSession;


    public StudentDTO toDTO() {
        return StudentDTO.builder()
                .id(id.toString())
                .currentSession(currentSession)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}