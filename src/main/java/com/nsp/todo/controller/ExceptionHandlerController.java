package com.nsp.todo.controller;

import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.*;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseContainer;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerController {


    @ExceptionHandler(UserException.class)
    public ResponseContainer handleUserException(UserException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(LanguageException.class)
    public ResponseContainer handleLanguageException(LanguageException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(TechnologyException.class)
    public ResponseContainer handleTechnologyException(TechnologyException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(CountryException.class)
    public ResponseContainer handleCountryException(CountryException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(CityException.class)
    public ResponseContainer handleCityException(CityException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(AnswerException.class)
    public ResponseContainer handleAnswerException(AnswerException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(TaskException.class)
    public ResponseContainer handleTaskException(TaskException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseContainer handleTokenException(TokenException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseContainer handleValidationException(ValidationException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(CvException.class)
    public ResponseContainer handleCvException(CvException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(AddressException.class)
    public ResponseContainer handleAddressException(AddressException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseContainer handleNotificationException(NotificationException ex){
        Response r = Response.builder()
                .status(ex.getStatus().name())
                .message(ex.getStatus().getMessage())
                .errors(ex.getErrors())
                .code(ex.getStatus().getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseContainer handleSocketTimeoutException(SocketTimeoutException ex){
        Response r = Response.builder()
                .status(Status.EMAIL_SENT_ERROR.name())
                .message(Status.EMAIL_SENT_ERROR.getMessage())
                .errors(new HashSet<>())
                .code(Status.EMAIL_SENT_ERROR.getCode())
                .build();
        return ResponseContainer.ok(r);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseContainer handleMethodArgumentNotValidException(BindingResult ex){
        Response r = Response.builder()
                .status(Status.VALIDATION_ERROR.name())
                .message(Status.VALIDATION_ERROR.getMessage())
                .errors(ex.getFieldErrors().stream().map(e->e.getField()+":"+e.getDefaultMessage()).collect(Collectors.toSet()))
                .code(Status.VALIDATION_ERROR.getCode())
                .build();
        return ResponseContainer.ok(r);
    }


}
