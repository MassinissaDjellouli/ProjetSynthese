package com.synthese.model;

import com.synthese.dto.ManagerDTO;
import org.bson.types.ObjectId;

public class Manager {
    private ObjectId id;
    private ObjectId userId;
    private String firstName;
    private String lastName;
    private String username;

    public ManagerDTO toDTO() {
        return ManagerDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .build();
    }
}