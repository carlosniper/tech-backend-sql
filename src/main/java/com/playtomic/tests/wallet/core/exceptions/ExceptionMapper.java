package com.playtomic.tests.wallet.core.exceptions;

import com.playtomic.tests.wallet.core.exceptions.wallet.WalletChargeException;
import com.playtomic.tests.wallet.core.exceptions.wallet.WalletNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class ExceptionMapper {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> validationHandle(MethodArgumentNotValidException e, WebRequest req) {
        log.error("Validation error: ", e);
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {WalletNotFoundException.class})
    public ResponseEntity<ErrorResponse> notFoundHandle(WalletNotFoundException e) {
        log.error("Not found error: ", e);
        ErrorResponse errorResponse = new ErrorResponse("Wallet not found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {WalletChargeException.class})
    public ResponseEntity<ErrorResponse> walletChargeHandle(WalletChargeException e) {
        log.error("Wallet charge error: {}", e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }
}
