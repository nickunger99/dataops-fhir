package com.dataops.api.domain.auth.user;

import com.dataops.api.domain.auth.role.Role;

import java.time.LocalDateTime;
import java.util.Set;

public record DetailsUser(
        Long id,
        String username,
        String email,
        String roleName,
        Set<Role> roles,
        String name,
        StatusUser status,
        LocalDateTime lastModified) {

    public DetailsUser(User user) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getRoleName(), user.getRoles(), user.getName(),
                user.getStatus(), user.getLastModified());
    }
}
