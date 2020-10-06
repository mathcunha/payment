package com.bank.payment.controller;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    Pattern UK_PATTERN = Pattern.compile("constraint \\[(?<constraint>.+)\\]");

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Bad Request");
        response.put("message", "ValidationError");
        response.put("data", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class, ConstraintViolationException.class})
    protected ResponseEntity handleIntegrityConstraint(RuntimeException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        Matcher matcher = UK_PATTERN.matcher(ex.getMessage());
        if(matcher.find()) {
            String constraint = matcher.group("constraint");
            errors.put(constraint.split("_")[2], "already in use");
        }else{
            errors.put("resource", ex.getMessage());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Bad Request");
        response.put("message", "AlreadyInUseError");
        response.put("data", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = {StaleObjectStateException.class})
    protected ResponseEntity optimisticLockingFailureException(RuntimeException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("version", "this info was update by other action. Please refresh");
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Bad Request");
        response.put("message", "OutdatedInfoError");
        response.put("data", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}