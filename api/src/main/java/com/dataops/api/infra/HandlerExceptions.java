package com.dataops.api.infra;


import com.dataops.api.domain.MessageResponse;
import com.dataops.api.domain.ValidatorException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class HandlerExceptions {
    private static Logger logger = LoggerFactory.getLogger(HandlerExceptions.class);

    @ExceptionHandler(EntityNotFoundException.class)

    public ResponseEntity Error404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity Error400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        logger.error(erros.toString());
        return ResponseEntity.badRequest().body(erros.stream().map(ValidationError::new).toList());
    }

    @ExceptionHandler(ValidatorException.class)
    public ResponseEntity Error400(ValidatorException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity Error400(HttpMessageNotReadableException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity ErrorBadCredentials() {
        logger.warn("Bad Credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Bad Credentials"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity ErrorAuthentication() {
        logger.warn("Authentication Failure");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Authentication Failure"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity ErrorAccessDenied() {
        logger.warn("Access denied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Access denied"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity Error500(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + ex.getLocalizedMessage());
    }

    private record ValidationError(String field, String message) {
        public ValidationError(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
