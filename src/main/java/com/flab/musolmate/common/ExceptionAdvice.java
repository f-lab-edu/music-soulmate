package com.flab.musolmate.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity< Map<String, List<String>>> validationNotValidException( MethodArgumentNotValidException e) {
        List< ObjectError > allErrors = e.getBindingResult().getAllErrors();

        Map<String, List<String>> errorMessageMap = new HashMap<>();
        List<String > errorMessages = new ArrayList<>();
        for( ObjectError error : allErrors ) {
            log.error( "error: {}", error );
            log.error( "error: {}", error.getDefaultMessage() );
            errorMessages.add( error.getDefaultMessage() );
        }
        errorMessageMap.put( "errors", errorMessages );
        return new ResponseEntity<>( errorMessageMap, HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(e.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }
}
