package com.calebhanselman.BusinessInfo.configuration;

import com.calebhanselman.BusinessInfo.utility.BusinessInfoExceptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessInfoExceptionHandler {

    /**
     * Static helper class to allow for templatiztion on the ResponseEntity class... its just how spring does it.
     */
    @Data
    @AllArgsConstructor
    public static class CustomError{
        private HttpStatus status;
        private String message;
    }

    @ExceptionHandler(BusinessInfoExceptions.ZipCodeException.class)
    public ResponseEntity<CustomError> handleException(BusinessInfoExceptions.ZipCodeException e){
        CustomError error = new CustomError(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(error, error.getStatus());
    }
}
