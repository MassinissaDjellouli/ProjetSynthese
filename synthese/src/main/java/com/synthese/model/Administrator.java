package com.synthese.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "administrators")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Administrator {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDateTime lastConnection;

}
