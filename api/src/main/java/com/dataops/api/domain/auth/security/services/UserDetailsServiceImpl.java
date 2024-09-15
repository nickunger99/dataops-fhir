package com.dataops.api.domain.auth.security.services;

import com.dataops.api.domain.auth.user.User;
import com.dataops.api.domain.auth.user.UserRepository;
import com.dataops.api.domain.auth.user.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    // @Autowired
    // private JavaMailSender mailSender;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public List<UserResponse> ListUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<UserResponse>();

        users.forEach(x -> {
            userResponses.add(new UserResponse(x.getId(), x.getUsername(), x.getEmail(), x.getRoles()));
        });
        return userResponses;
    }

}