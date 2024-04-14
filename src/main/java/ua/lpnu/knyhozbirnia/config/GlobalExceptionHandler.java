package ua.lpnu.knyhozbirnia.config;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": " + e.getMessage() + "}");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body("{\"error\": " + e.getMessage() + "}");
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<String> handleException(LoginFailedException e) {
        return ResponseEntity.badRequest().body("{\"error\": " + e.getMessage() + "}");
    }

    @ExceptionHandler(AuthorizationServiceException.class)
    public ResponseEntity<String> handleException(AuthorizationServiceException e) {
        return ResponseEntity.badRequest().body("{\"error\": " + e.getMessage() + "}");
    }
}