package com.userservice.global;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {


    // 직접 정의한 에러
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException e) {
        log.error("[ Auth Service - GlobalExceptionHandler ] CustomException errorResponse : {}", e.getResponseCode().getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getResponseCode());
        return ResponseEntity
                .status(e.getResponseCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException() {
        log.error("[ Auth Service - GlobalExceptionHandler ] Exception errorResponse : {}", ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity
                .status(ResponseCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(errorResponse);
    }

    //validation exception 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> processValidationError(MethodArgumentNotValidException e) {
        log.error("[ Auth Service - GlobalExceptionHandler ] MethodArgumentNotValidException errorResponse : {}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponse);
    }

    //잘못된 자료형으로 인한 에러
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> methodArgumentTypeMismatchExceptionError(
            MethodArgumentTypeMismatchException e) {
        log.error("[ Auth Service - GlobalExceptionHandler ] MethodArgumentTypeMismatchException errorResponse : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.BAD_REQUEST, e);
        return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.getStatus())
                .body(errorResponse);
    }

    //잘못된 자료형으로 인한 에러
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e) {
        log.error("[ Auth Service - GlobalExceptionHandler ] HttpMessageNotReadableException errorResponse : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.BAD_REQUEST, e);
        return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.getStatus())
                .body(errorResponse);

    }


    //외부 api client 에러
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> httpMediaTypeNotSupportedExceptionError(
            HttpClientErrorException e) {
        log.error("[ Auth Service - GlobalExceptionHandler ] HttpClientErrorException errorResponse : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.INTERNAL_SERVER_ERROR, e);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponse);
    }

    //외부 api server 에러
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> httpServerErrorExceptionError(HttpServerErrorException e) {
        log.error("[ Auth Service - GlobalExceptionHandler ] HttpServerErrorException errorResponse : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.INTERNAL_SERVER_ERROR, e);
        log.info("HttpServerErrorException errorResponse : {}", errorResponse.getMessage());
        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponse);
    }

}
