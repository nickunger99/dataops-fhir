package com.dataops.api.domain.auth.role;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role(RegisterRole role) {
        this.name = role.name();
    }

    public Role(ERole eRole) {
        this.name = ERole.valueOf(eRole.name());
    }
}