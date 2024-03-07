package com.flab.musolmate.common.exception;

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

import static com.flab.musolmate.common.exception.ExceptionEnum.*;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity< Object > handleMethodArgumentNotValid( MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request ) {
        String objectName = ex.getBindingResult().getObjectName();
        int statusCode = METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getStatusCode();
        if ( objectName.startsWith( "member" ) ) {
            statusCode = METHOD_ARGUMENT_MEMBER_NOT_VALID_EXCEPTION.getStatusCode();
        }
        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( ApiResponse.createFail( statusCode, ex.getBindingResult() ) );
    }

    @ExceptionHandler( DuplicateMemberException.class )
    public ResponseEntity< ApiResponse< ? > > DuplicateMemberException( RuntimeException exception ) {
        return ResponseEntity.status( HttpStatus.CONFLICT ).body( ApiResponse.createError( DUPLICATE_MEMBER_EXCEPTION.getStatusCode(), exception.getMessage() ) );
    }
    @ExceptionHandler( NotFoundMemberException.class )
    public ResponseEntity< ApiResponse< ? > > NotFoundMemberException( RuntimeException exception ) {
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( ApiResponse.createError( NOT_FOUND_MEMBER_EXCEPTION.getStatusCode(), exception.getMessage() ) );
    }

    @ExceptionHandler( { AuthenticationException.class } )
    @ResponseBody
    public ResponseEntity< ApiResponse< ? > > handleAuthenticationException( AuthenticationException ex ) {
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( ApiResponse.createError( UNAUTHORIZED_EXCEPTION.getStatusCode(), ex.getMessage() ) );

    }

    @ExceptionHandler( { AccessDeniedException.class } )
    @ResponseBody
    public ResponseEntity< ApiResponse< ? > > handleAccessDeniedException( AccessDeniedException ex ) {
        return ResponseEntity.status( HttpStatus.FORBIDDEN ).body( ApiResponse.createError( FORBIDDEN_EXCEPTION.getStatusCode(), ex.getMessage() ) );

    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity< ApiResponse< ? > > handleException( Exception ex ) {
        log.error( "Exception", ex );
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( ApiResponse.createError( INTERNAL_SERVER_ERROR.getStatusCode(), ex.getMessage() ) );
    }
}
