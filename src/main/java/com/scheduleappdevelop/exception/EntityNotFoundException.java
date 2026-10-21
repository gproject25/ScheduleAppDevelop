package com.scheduleappdevelop.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ServiceException{
    public EntityNotFoundException(String message){
        super(HttpStatus.NOT_FOUND, message);
    }
}
