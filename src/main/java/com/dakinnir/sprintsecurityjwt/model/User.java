package com.dakinnir.sprintsecurityjwt.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Document(collection = "users")
@Builder
public class User {
    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    @Indexed(unique = true)
    @Email
    private String email;
    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private boolean enabled;
    private String verificationToken;

}
