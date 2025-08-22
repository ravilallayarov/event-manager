package org.example.eventmanager.security.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "login cannot be blank")
    @Size(min = 5, max = 30, message = "login must be at least 5 and no more than 30 characters")
    private String login;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 5, max = 20, message = "password must be at least 5 and no more than 20 characters")
    private String password;

    @NotNull(message = "age cannot be null")
    @Min(value = 18, message = "age must be at least 18")
    @Max(value = 100, message = "age not more than 100")
    private Integer age;
}
