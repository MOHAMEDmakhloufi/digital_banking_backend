package com.fsb.digital_banking_backend.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;

import java.time.Instant;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) {
        super(message, null);
    }
}
