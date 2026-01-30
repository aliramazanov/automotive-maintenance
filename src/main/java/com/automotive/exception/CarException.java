package com.automotive.exception;

import com.automotive.exception.base.BaseErrorService;
import com.automotive.exception.base.BaseException;
import lombok.Getter;

import java.io.Serial;

@Getter
public class CarException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CarException(BaseErrorService baseErrorService, Throwable throwable, Object... args) {
        super(baseErrorService, throwable, args);
    }

    public CarException(BaseErrorService baseErrorService, Object... args) {
        super(baseErrorService, args);
    }
}
