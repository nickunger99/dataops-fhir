package com.dataops.api.domain.auth.user;

import com.dataops.api.domain.auth.role.Role;

import java.util.Set;

public class UserResponse {
    private Long id;

    private String username;

    private String email;

    private Set<Role> role;

    public UserResponse(Long id, String username, String email, Set<Role> role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }
}
