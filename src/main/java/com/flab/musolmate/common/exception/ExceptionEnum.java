package com.flab.musolmate.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionEnum {

        UNAUTHORIZED_EXCEPTION(40100 ),
        FORBIDDEN_EXCEPTION(40300 ),
        INTERNAL_SERVER_ERROR(50000 ),
        METHOD_ARGUMENT_NOT_VALID_EXCEPTION(40000 ),

        // Member관련 : XXX01
        METHOD_ARGUMENT_MEMBER_NOT_VALID_EXCEPTION(40001 ),
        DUPLICATE_MEMBER_EXCEPTION(40901 ),
        NOT_FOUND_MEMBER_EXCEPTION(40401 );


        private final int statusCode;

        ExceptionEnum(int statusCode) {
            this.statusCode = statusCode;
        }
}
