package com.example.dmaker01.exception;

import com.example.dmaker01.dto.DMakerErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(
            DMakerException e,
            HttpServletRequest request
    ) {
        log.error("errorCode : {}, url : {}, message : {}",
                e.getDMakerErrorCode(), request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorMessage(e.getDetailMessage())
                .errorCode(e.getDMakerErrorCode())
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class,
    })
    public DMakerErrorResponse handleBadRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("url : {}, message : {}",
                request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorMessage(DMakerErrorCode.INVALID_REQUEST.getMessage())
                .errorCode(DMakerErrorCode.INVALID_REQUEST)
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResponseStatusException.class)
    public DMakerErrorResponse handleException(
            ResponseStatusException e, HttpServletRequest request
    ){
        log.error("uri : {}, message : {}", request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorMessage(e.getMessage())
                .errorCode(DMakerErrorCode.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleException(
            Exception e, HttpServletRequest request
    ) {
        log.error("uri : {}, errorCode : {}", request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorMessage(DMakerErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .errorCode(DMakerErrorCode.INTERNAL_SERVER_ERROR)
                .build();
    }
}
