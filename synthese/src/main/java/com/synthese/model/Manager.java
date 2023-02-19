package com.synthese.model;

import com.synthese.dto.ManagerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "managers")
public class Manager {
    private ObjectId id;
    private ObjectId userId;
    private String firstName;
    private String lastName;
    private String username;

    public ManagerDTO toDTO() {
        return ManagerDTO.builder()
                .id(id.toString())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .build();
    }
}