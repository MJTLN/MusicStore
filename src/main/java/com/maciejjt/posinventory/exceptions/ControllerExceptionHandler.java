package com.maciejjt.posinventory.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> superException(Exception e) {
        e.printStackTrace();

        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(DeletionException.class)
    public ResponseEntity<ApiError> handleStorageNotEmptiedException(DeletionException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(WarehouseConflictException.class)
    public ResponseEntity<ApiError> handleWarehouseConflictException(WarehouseConflictException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(BadStatusException.class)
    public ResponseEntity<ApiError> ShipmentAlreadyFinalizedException(BadStatusException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }
}