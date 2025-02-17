package br.com.pedrovictor.sistlog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<Map<String, String>> handleInvalidStatusException(InvalidStatusException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Warning: ", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error: ", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error: ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }
}
