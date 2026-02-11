package com.automotive.exception;

import com.automotive.exception.base.BaseErrorEnum;
import com.automotive.exception.base.BaseErrorResponseDTO;
import com.automotive.exception.base.BaseException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponseDTO>
    handleValidationException (MethodArgumentNotValidException ex, WebRequest webRequest)
    {
        Map<String, String> errors = new HashMap<>();
      
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return new ResponseEntity<>(
                new BaseErrorResponseDTO(
                        BaseErrorEnum.BASE_VALIDATION_ERROR.getErrorCode(),
                        BaseErrorEnum.BASE_VALIDATION_ERROR.getMessage(),
                        webRequest.getContextPath(),
                        LocalDateTime.now().toString(),
                        BaseErrorEnum.BASE_VALIDATION_ERROR.getHttpStatus(),
                        errors
                ),
                HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseErrorResponseDTO>
    handleAuthenticationException (AuthenticationException ex, WebRequest webRequest)
    {
        return new ResponseEntity<>(
                new BaseErrorResponseDTO(
                        AuthErrorEnum.INVALID_CREDENTIALS.getErrorCode(),
                        ex.getMessage(),
                        webRequest.getContextPath(),
                        LocalDateTime.now().toString(),
                        AuthErrorEnum.INVALID_CREDENTIALS.getHttpStatus()
                ),
                HttpStatus.UNAUTHORIZED
        );
    }
    
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<BaseErrorResponseDTO>
    handleJwtException (WebRequest webRequest)
    {
        return new ResponseEntity<>(
                new BaseErrorResponseDTO(
                        AuthErrorEnum.INVALID_TOKEN.getErrorCode(),
                        AuthErrorEnum.INVALID_TOKEN.getMessage(),
                        webRequest.getContextPath(),
                        LocalDateTime.now().toString(),
                        AuthErrorEnum.INVALID_TOKEN.getHttpStatus()
                ),
                HttpStatus.UNAUTHORIZED
        );
    }
    
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseErrorResponseDTO>
    handleBaseException (BaseException ex, WebRequest webRequest)
    {
        return new ResponseEntity<>(
                new BaseErrorResponseDTO(
                        ex.baseErrorService.getErrorCode(),
                        ex.baseErrorService.getMessage(),
                        webRequest.getContextPath(),
                        LocalDateTime.now().toString(),
                        ex.baseErrorService.getHttpStatus()
                ),
                HttpStatusCode.valueOf(ex.baseErrorService.getHttpStatus())
        );
    }
}
