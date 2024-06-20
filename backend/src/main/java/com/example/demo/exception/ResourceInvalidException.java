package com.example.demo.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceInvalidException extends Exception {

    private static final long serialVersionUID = 1L;

    public ResourceInvalidException(String message, DataIntegrityViolationException exception) {
        super(message, exception);
    }
}