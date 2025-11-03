package com.recsys.recPet.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    @ResponseStatus(HttpStatus.CONFLICT) // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Map<String, String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        String rootMessage = ex.getMostSpecificCause().getMessage();

        if (rootMessage.contains("uc_usuarios_email")) {
            errorMap.put("error", "Este e-mail já está em uso.");
        }
        else if (rootMessage.contains("uc_usuarios_cpf")) {
            errorMap.put("error", "Este CPF já está em uso.");
        }
        else {
            errorMap.put("error", "Conflito de dados. Um valor único já existe.");
        }

        return errorMap;
    }
}