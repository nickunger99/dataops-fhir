package com.dataops.api.domain.auth.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(ERole.valueOf(name));
    }

    public void initialize() {
        if (findByName("ROLE_USER").isEmpty()) {
            Role user = new Role(ERole.ROLE_USER);
            save(user);
        }
        if (findByName("ROLE_MODERATOR").isEmpty()) {
            Role user = new Role(ERole.ROLE_MODERATOR);
            save(user);
        }
        if (findByName("ROLE_ADMIN").isEmpty()) {
            Role user = new Role(ERole.ROLE_ADMIN);
            save(user);
        }
    }
}
