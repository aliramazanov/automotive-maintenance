package com.automotive.exception.base;

public interface BaseErrorService {

    String getMessage();

    int getHttpStatus();

    String getErrorCode();
}
