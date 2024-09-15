package com.dataops.api.domain.auth.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Usuário não deve estar em branco")
    private String username;

    @NotBlank(message = "Senha não deve estar em branco")
    private String password;
}
