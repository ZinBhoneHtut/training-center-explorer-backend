package com.zbh.tce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        ErrorResponse errorDetail = new ErrorResponse(
                new Date(),
                HttpStatus.NOT_FOUND.toString(),
                exception.getMessage(),
                "",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(BadRequestException exception, WebRequest request) {
        ErrorResponse errorDetail = new ErrorResponse(
                new Date(),
                exception.getStatus().toString(),
                exception.getMessage(),
                "",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetail, exception.getStatus());
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    public ResponseEntity<ErrorResponse> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(
                new Date(),
                HttpStatus.FORBIDDEN.toString(),
                ex.getMessage(),
                "",
                request.getDescription(false)
        ), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameterException(MissingServletRequestParameterException exception, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.name(),
                exception.getMessage(),
                "",
                request.getDescription(false)
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception, WebRequest request) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(new ErrorResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.name(),
                "Validation error",
                String.join(", ", errorMessages),
                request.getDescription(false)
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException exception, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(
                new Date(),
                HttpStatus.UNAUTHORIZED.name(),
                exception.getMessage(),
                "Invalid username or password",
                request.getDescription(false)
        ), HttpStatus.UNAUTHORIZED);
    }

}
