package com.dataops.api.domain.auth.user;

public record ChangePassword(
        Long id,
        String username,
        String currentPassword,
        String newPassword

) {
}
