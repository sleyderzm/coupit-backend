package com.allcode.coupit.Coupit.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

public class ConstraintViolationExceptionHandler {
    public static ResponseEntity<ErrorResponse> getResponse(ConstraintViolationException ex){
        String message = "Invalid value for fields [";
        Set<ConstraintViolation<?>> cvs = ex.getConstraintViolations();
        for(ConstraintViolation<?> cv : cvs){
            String field = cv.getPropertyPath().toString();
            message +=  field +",";
        }
        message = message.substring(0,message.length() - 1) + ']';
        ErrorResponse error = new ErrorResponse(message);
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }
}