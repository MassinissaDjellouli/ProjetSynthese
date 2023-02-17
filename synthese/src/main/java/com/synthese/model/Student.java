package com.synthese.model;

import com.synthese.dto.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "students")
@AllArgsConstructor
@Builder
@Data
public class Student {
    private ObjectId id;
    private ObjectId userId;
    private String firstName;
    private String lastName;
    private String username;
    private int currentSession;

    public StudentDTO toDTO() {
        return StudentDTO.builder()
                .currentSession(currentSession)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .build();
    }
}