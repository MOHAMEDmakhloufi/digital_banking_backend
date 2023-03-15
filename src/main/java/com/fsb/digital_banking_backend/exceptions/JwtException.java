package com.fsb.digital_banking_backend.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
