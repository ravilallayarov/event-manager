package org.example.eventmanager.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank(message = "login cannot be blank")
    private String login;

    @NotBlank(message = "password cannot be blank")
    private String password;
}
