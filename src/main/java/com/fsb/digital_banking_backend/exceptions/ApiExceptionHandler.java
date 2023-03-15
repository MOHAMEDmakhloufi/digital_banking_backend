package com.fsb.digital_banking_backend.exceptions;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {AmountNotVallidException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }
    @ExceptionHandler(value = {BalanceNotSufficientException.class})
    public ResponseEntity<Object> handleNotAcceptableException(Exception e){
        HttpStatus notAcceptable = HttpStatus.NOT_ACCEPTABLE;

        ApiException apiException = new ApiException(
                e.getMessage(),
                notAcceptable,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, notAcceptable);
    }

    @ExceptionHandler(value = {BankAccountNotFoundException.class, CustomerNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception e){
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, notFound);
    }
    @ExceptionHandler(value = {ServletException.class})
    public ResponseEntity<Object> handleServletException(Exception e){
        HttpStatus unAuthorized= HttpStatus.UNAUTHORIZED;
        ApiException apiException = new ApiException(
                e.getMessage(),
                unAuthorized,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, unAuthorized);
    }

    @ExceptionHandler(value = {JwtException.class})
    public ResponseEntity<Object> handleJWTVerificationException(Exception e){
        HttpStatus forbidden= HttpStatus.FORBIDDEN;
        ApiException apiException = new ApiException(
                e.getClass().getSimpleName(),
                forbidden,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, forbidden);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e){
        HttpStatus internalServerError= HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(
                "An error occurred",
                internalServerError,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, internalServerError);
    }

}
