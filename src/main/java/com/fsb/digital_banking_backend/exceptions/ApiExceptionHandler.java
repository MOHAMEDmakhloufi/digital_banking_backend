package com.fsb.digital_banking_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}
