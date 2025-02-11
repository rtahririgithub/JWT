package com.telus.starter.springboot.service;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JwtServiceErrorAdvice { @ExceptionHandler({RuntimeException.class})
    
    public ResponseEntity<String> handleRunTimeException(RuntimeException e) {
        return error(INTERNAL_SERVER_ERROR, e);
    }
	public ResponseEntity<String> handleSecurityException(SecurityException e) {
	    return error(UNAUTHORIZED, e);
	}
    private ResponseEntity<String> error(HttpStatus status, Exception e) {
         return ResponseEntity.status(status).body(e.getMessage());
    }
}