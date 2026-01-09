package com.interview.assignment.domain.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorObject> handleNotFound(ResourceNotFoundException ex) {
        log.error("ResourceNotFoundException message :{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorObject("ERR_101", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorObject> handleBusiness(BusinessException ex) {
        log.error("BusinessException message :{}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorObject("ERR_102", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleAll(Exception ex, HttpServletRequest req) {
        log.error("Generic error :{} from request :{}", ex.getMessage(), req.getContextPath());
        return ResponseEntity.internalServerError()
                .body(new ErrorObject("ERR_100", "Unexpected error :"+ex.getMessage(), LocalDateTime.now()));
    }
}
