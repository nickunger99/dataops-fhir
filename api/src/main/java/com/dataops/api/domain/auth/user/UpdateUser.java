package com.dataops.api.domain.auth.user;

import jakarta.validation.constraints.Email;
import lombok.Getter;

import java.util.Set;

@Getter
public class UpdateUser {
    private Long id;
    // @NotBlank
    // @Size(min = 3, max = 20)
    private String username;
    // @NotBlank
    // @Size(max = 50)
    @Email
    private String email;
    private Set<String> roles;
    private String name;
    private StatusUser status;
}
