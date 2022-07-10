package com.artplan.pets.dto;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private String error;
    private Integer status;
    private List<String> messages;
    
    public ExceptionResponse(HttpStatus status, String ...messages) {        
        this.error = status.getReasonPhrase();
        this.status = status.value();
        this.messages = Arrays.asList(messages);
    }
    
    public ExceptionResponse(HttpStatus status, List<String> messages) {        
        this.error = status.getReasonPhrase();
        this.status = status.value();
        this.messages = messages;
    }
    
}
