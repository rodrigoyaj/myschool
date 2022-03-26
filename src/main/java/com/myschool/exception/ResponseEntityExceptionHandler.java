package com.myschool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ResponseEntityExceptionHandler{
    @ExceptionHandler(value = { StudentNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected  ErrorMessage handleException(StudentNotFoundException ex, WebRequest request) {
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Student not found.");
    }
}
