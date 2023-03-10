package com.synthese.model;

import com.synthese.dto.TeacherDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
    private ObjectId establishment;
    private List<ObjectId> courses;


    public TeacherDTO toDTO(List<Course> courses) {
        return TeacherDTO.builder()
                .id(id.toString())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .courses(courses.stream().map(Course::toDTO).toList())
                .build();
    }

    public TeacherDTO toDTO(String establishment, List<Course> courses) {
        return TeacherDTO.builder()
                .id(id.toString())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .establishmentId(establishment)
                .courses(courses.stream().map(Course::toDTO).toList())
                .build();
    }

    public TeacherDTO toDTO(String establishment) {
        return TeacherDTO.builder()
                .id(id.toString())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .establishmentId(establishment)
                .courses(List.of())
                .build();
    }
}