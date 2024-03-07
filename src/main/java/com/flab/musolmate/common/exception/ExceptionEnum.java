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

        public static final String MESSAGE_DUPLICATE_MEMBER_EXCEPTION_EMAIL = "이미 존재하는 이메일입니다.";
        public static final String MESSAGE_DUPLICATE_MEMBER_EXCEPTION_NICKNAME = "이미 존재하는 닉네임입니다.";

        private final int statusCode;

        ExceptionEnum(int statusCode) {
            this.statusCode = statusCode;
        }
}
