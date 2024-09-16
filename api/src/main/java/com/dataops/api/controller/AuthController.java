package com.dataops.api.controller;

import com.dataops.api.domain.MessageResponse;
import com.dataops.api.domain.auth.role.RoleRepository;
import com.dataops.api.domain.auth.security.jwt.JwtUtils;
import com.dataops.api.domain.auth.security.services.UserDetailsImpl;
import com.dataops.api.domain.auth.security.services.UserDetailsServiceImpl;
import com.dataops.api.domain.auth.user.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Optional<User> userOptional = userService.findByUsername(loginRequest.getUsername());
        if (userOptional.isEmpty()) {
            logger.warn("User: " + loginRequest.getUsername() + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
        }

        if (userOptional.get().getStatus() != StatusUser.ATIVO) {
            logger.warn("User: " + loginRequest.getUsername() + " inactive");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("inactive user, please contact your system administrator"));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(
                new UserInfoResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        userDetails.getRoleName(),
                        userDetails.getName(),
                        userDetails.getStatus()));
    }

    @PostMapping("/signup")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity registerUser(@Valid @RequestBody RegisterUser signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            logger.warn("User: " + signUpRequest.getUsername() + " in use");
            return ResponseEntity.badRequest().body(new MessageResponse("User is in use"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            logger.warn("E-mail: " + signUpRequest.getEmail() + " in use");
            return ResponseEntity.badRequest().body(new MessageResponse("E-mail is in use"));
        }

        String passEncode = encoder.encode(signUpRequest.getPassword());
        User user = userService.registerUser(signUpRequest, passEncode);
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("User: " + user.getName() + " successfully registered"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new MessageResponse("Logout successfully"));
    }
}
