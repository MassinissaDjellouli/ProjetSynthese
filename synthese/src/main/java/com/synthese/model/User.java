package com.synthese.model;

import com.synthese.security.Roles;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    private ObjectId id;

    @NotBlank
    @Indexed(unique = true)
    @Size(min = 6, max = 12)
    private String username;
    @NotBlank
    @Size(min = 8)
    private String password;

    @NotNull
    private Roles role;

    @NotNull
    private LocalDateTime expirationDate;

    @NotNull
    private LocalDateTime credentialsExpirationDate;

    @NotNull
    private boolean locked;


}
