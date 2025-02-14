package com.ECounselling.exception;

import com.ECounselling.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception e) {
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage()
        );
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }

    @ExceptionHandler(CollegeNotFoundException.class)
    public ResponseEntity<ApiError> handleCollegeNotFoundException(Exception e) {
        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiError> handleStudentNotFoundException(Exception e) {
        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }
}
