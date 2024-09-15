package com.dataops.api.domain.auth.user;

import com.dataops.api.domain.auth.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 20)
    @Column(unique = true)
    private String username;
    @NotBlank
    @Size(max = 50)
    @Email
    @Column(unique = true)
    private String email;

    private String password;
    @Setter
    private String roleName;
    private String name;
    @Enumerated(EnumType.STRING)
    private StatusUser status;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;


    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(RegisterUser signUpRequest, String password) {
        this.username = signUpRequest.getUsername();
        this.email = signUpRequest.getEmail();
        this.password = password;
        this.name = signUpRequest.getName();
        this.status = StatusUser.ATIVO;
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC-3"));
        this.lastModified = LocalDateTime.now(ZoneId.of("UTC-3"));
    }

    public User(UpdateUser updateUser) {
        if (updateUser.getUsername() != null)
            this.username = updateUser.getUsername();
        if (updateUser.getEmail() != null)
            this.email = updateUser.getEmail();
        if (updateUser.getName() != null)
            this.name = updateUser.getName();
        if (updateUser.getStatus() != null)
            this.status = updateUser.getStatus();
        this.lastModified = LocalDateTime.now(ZoneId.of("UTC-3"));
    }

    public User(User user, String passEncode) {
        if (user.getUsername() != null)
            this.username = user.getUsername();
        if (user.getEmail() != null)
            this.email = user.getEmail();
        if (user.getName() != null)
            this.name = user.getName();
        if (passEncode != null)
            this.password = passEncode;
        this.lastModified = LocalDateTime.now(ZoneId.of("UTC-3"));
    }

    public void updateUser(UpdateUser updateUser) {
        if (updateUser.getUsername() != null)
            this.username = updateUser.getUsername();
        if (updateUser.getEmail() != null)
            this.email = updateUser.getEmail();
        if (updateUser.getName() != null)
            this.name = updateUser.getName();
        if (updateUser.getStatus() != null)
            this.status = updateUser.getStatus();
        this.lastModified = LocalDateTime.now(ZoneId.of("UTC-3"));
    }

    public void changePassword(String passEncode) {
        this.password = passEncode;
        this.lastModified = LocalDateTime.now(ZoneId.of("UTC-3"));
    }
}
