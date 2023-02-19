package com.synthese.model;

import com.synthese.dto.TeacherDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "teachers")
public class Teacher {
    private ObjectId id;
    private ObjectId userId;
    private String firstName;
    private String lastName;
    private String username;


    public TeacherDTO toDTO() {
        return TeacherDTO.builder()
                .id(id.toString())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .build();
    }
}