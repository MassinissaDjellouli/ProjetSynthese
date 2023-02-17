package com.synthese.model;

import com.synthese.dto.AdminstratorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "administrators")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Administrator {
    private ObjectId id;
    private ObjectId userId;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime lastConnection;

    public AdminstratorDTO toDTO() {
        return AdminstratorDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .build();
    }
}