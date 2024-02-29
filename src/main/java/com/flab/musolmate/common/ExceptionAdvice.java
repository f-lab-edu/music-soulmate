package com.flab.musolmate.common;

import com.flab.musolmate.common.domain.api.ApiResponse;
import com.flab.musolmate.member.exception.DuplicateMemberException;
import com.flab.musolmate.member.exception.NotFoundMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity< Object > handleMethodArgumentNotValid( MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request ) {
        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( ApiResponse.createFail( HttpStatus.BAD_REQUEST.value(), ex.getBindingResult() ) );
    }

    @ExceptionHandler( DuplicateMemberException.class )
    public ResponseEntity< ApiResponse< ? > > DuplicateMemberException( RuntimeException exception ) {
        return ResponseEntity.status( HttpStatus.CONFLICT ).body( ApiResponse.createError( HttpStatus.CONFLICT.value(), exception.getMessage() ) );
    }
    @ExceptionHandler( NotFoundMemberException.class )
    public ResponseEntity< ApiResponse< ? > > NotFoundMemberException( RuntimeException exception ) {
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( ApiResponse.createError( HttpStatus.NOT_FOUND.value(), exception.getMessage() ) );
    }

    @ExceptionHandler( { AuthenticationException.class } )
    @ResponseBody
    public ResponseEntity< ApiResponse< ? > > handleAuthenticationException( AuthenticationException ex ) {
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( ApiResponse.createError( HttpStatus.UNAUTHORIZED.value(), ex.getMessage() ) );

    }

    @ExceptionHandler( { AccessDeniedException.class } )
    @ResponseBody
    public ResponseEntity< ApiResponse< ? > > handleAccessDeniedException( AccessDeniedException ex ) {
        return ResponseEntity.status( HttpStatus.FORBIDDEN ).body( ApiResponse.createError( HttpStatus.FORBIDDEN.value(), ex.getMessage() ) );

    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity< ApiResponse< ? > > handleException( Exception ex ) {
        log.error( "Exception", ex );
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( ApiResponse.createError( HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage() ) );
    }
}
