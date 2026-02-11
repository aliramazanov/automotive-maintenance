package com.automotive.exception;

import com.automotive.exception.base.BaseErrorService;

public enum AuthErrorEnum implements BaseErrorService {
    
    INVALID_TOKEN("AUTH-0001", "Invalid token", 401),
    USERNAME_EXISTS("AUTH-0002", "Username already exists", 400),
    USER_NOT_EXISTS("AUTH-0003", "User not found", 401),
    INVALID_CREDENTIALS("AUTH-0004", "Invalid credentials", 401),
    ROLE_NOT_FOUND("AUTH-0005", "Default role not found", 500);
    
    private final String errorCode;
    private final String message;
    private final int httpStatus;
    
    AuthErrorEnum (String errorCode, String message, int httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }
    
    @Override
    public String getMessage () {
        return message;
    }
    
    @Override
    public int getHttpStatus () {
        return httpStatus;
    }
    
    @Override
    public String getErrorCode () {
        return errorCode;
    }
}
