package com.ridehailing.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RideHailingException extends RuntimeException {
    public RideHailingException(String message) {
        super(message);
    }
}
