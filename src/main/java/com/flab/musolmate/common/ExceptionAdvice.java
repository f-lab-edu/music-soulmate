package com.flab.musolmate.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

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
    }

    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<Map<String, List<String>>> handleAuthenticationException( AuthenticationException ex ) {
        Map<String, List<String>> errorMessageMap = new HashMap<>();
        errorMessageMap.put( "errors", Collections.singletonList( ex.getMessage() ) );

        return new ResponseEntity<>( errorMessageMap, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseBody
    public ResponseEntity<Map<String, List<String>>> handleAccessDeniedException( AccessDeniedException ex ) {
        Map<String, List<String>> errorMessageMap = new HashMap<>();
        errorMessageMap.put( "errors", Collections.singletonList( ex.getMessage() ) );

        return new ResponseEntity<>( errorMessageMap, HttpStatus.FORBIDDEN );
    }
}
