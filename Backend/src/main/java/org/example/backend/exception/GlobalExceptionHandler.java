package org.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Gestion des erreurs de validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Collecte des messages d'erreur pour chaque champ non valide
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Gestion des violations de contraintes (par exemple : email ou numéro de téléphone déjà utilisé)
//    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
//    public ResponseEntity<String> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex) {
//        if (ex.getMessage().contains("unique_phone_number")) {
//            return new ResponseEntity<>("Phone number already in use", HttpStatus.CONFLICT);
//        }
//        if (ex.getMessage().contains("users_email_key")) {
//            return new ResponseEntity<>("Email already in use", HttpStatus.CONFLICT);
//        }
//        return new ResponseEntity<>("A unique constraint has been violated", HttpStatus.CONFLICT);
//    }

    // Gestion des exceptions générales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
