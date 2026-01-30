package com.automotive.exception.base;

import java.io.Serial;

public abstract class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Object[] args;
    public final BaseErrorService baseErrorService;

    protected BaseException(BaseErrorService baseErrorService, Throwable throwable, Object... args) {
        super(baseErrorService.getMessage(), throwable);
        this.baseErrorService = baseErrorService;
        this.args = args;
    }

    protected BaseException(BaseErrorService baseErrorService, Object... args) {
        super(baseErrorService.getMessage());
        this.baseErrorService = baseErrorService;
        this.args = args;
    }
}
