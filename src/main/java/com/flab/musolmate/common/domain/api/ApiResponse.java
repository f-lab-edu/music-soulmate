package com.flab.musolmate.common.domain.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ApiResponse<T> {
    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAIL = "fail";
    private static final String STATUS_ERROR = "error";

    private String status;
    private int code;
    private T data;

    public ApiResponse( String status, int code, T data ) {
        this.status = status;
        this.code = code;
        this.data = data;
    }

    public static <T> ApiResponse<T> createSuccess( T data ) {
        return new ApiResponse<>( STATUS_SUCCESS, HttpStatus.OK.value(), data );
    }

    public static ApiResponse< ? > createFail( int statusCode, BindingResult bindingResult ) {
        Map< String, String > errors = new HashMap<>();

        List< ObjectError > allErrors = bindingResult.getAllErrors();
        for ( ObjectError error : allErrors ) {

            if ( error instanceof FieldError ) {
                errors.put( ( ( FieldError ) error ).getField(), error.getDefaultMessage() );
            }
            else {
                errors.put( error.getObjectName(), error.getDefaultMessage() );
            }
        }
        return new ApiResponse<>( STATUS_FAIL, statusCode, errors );
    }

    public static ApiResponse<?> createError( int statusCode, String message) {
        return new ApiResponse<>( STATUS_ERROR, statusCode, message);
    }

}
