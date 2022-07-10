package com.artplan.pets.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.artplan.pets.dto.ExceptionResponse;
import com.artplan.pets.exception.BadRequestException;
import com.artplan.pets.exception.FailAttemptsLimitIsOverException;
import com.artplan.pets.exception.ResourceNotFoundException;
import com.artplan.pets.exception.UnauthorizedException;

@ControllerAdvice
public class ExceptionHandlerRestController {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(UnauthorizedException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(ResourceNotFoundException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND, exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(BadRequestException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FailAttemptsLimitIsOverException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(FailAttemptsLimitIsOverException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }    
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> messages = new ArrayList<>(fieldErrors.size());
        for (FieldError error : fieldErrors) {
            messages.add(error.getField() + " - " + error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST, messages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(MethodArgumentTypeMismatchException ex) {
        String message = "Parameter '" + ex.getParameter().getParameterName() + "' must be '"
                + Objects.requireNonNull(ex.getRequiredType()).getSimpleName() + "'";
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(HttpRequestMethodNotSupportedException ex) {
        String message = "Request method '" + ex.getMethod() + "' not supported. List of all supported methods - "
                + ex.getSupportedHttpMethods();
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED, message),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(HttpMessageNotReadableException ex) {
        String message = "Please provide Request Body in valid JSON format";
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

}
