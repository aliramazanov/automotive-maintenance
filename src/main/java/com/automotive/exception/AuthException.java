package com.automotive.exception;

import com.automotive.exception.base.BaseErrorService;
import com.automotive.exception.base.BaseException;
import lombok.Getter;

import java.io.Serial;

@Getter
public class AuthException extends BaseException {
    
    @Serial
    private static final long serialVersionUID = 2L;
    
    public AuthException (BaseErrorService baseErrorService, Object... args) {
        super(baseErrorService, args);
    }
}
