package com.scheduleappdevelop.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ApiException {
    public EntityNotFoundException(String message){
        super(HttpStatus.NOT_FOUND, message);
    }
}
