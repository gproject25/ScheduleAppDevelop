package com.scheduleappdevelop.config;

import com.scheduleappdevelop.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 현재 상태로 인해 요청한 작업을 수행할 수 없는 경우
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("요청 오류: " + e.getMessage());
    }

    // 메서드에 전달된 인자가 유효하지 않은 경우
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("요청 오류: " + e.getMessage());
    }

    //
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("올바른 형식으로 입력하세요");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFound(
            NoResourceFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("요청한 경로를 찾을 수 없습니다.");
    }

    //validation 에러 handling
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("입력 값이 올바르지 않습니다.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    //service 관련 에러 handling
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleServiceException(ApiException e){
        return ResponseEntity
                .status(e.getStatus())
                .body(e.getMessage());
    }

}
