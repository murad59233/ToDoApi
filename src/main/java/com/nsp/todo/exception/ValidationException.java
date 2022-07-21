package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

import java.util.Set;

public class ValidationException extends BaseException{
    public ValidationException(Status status, Set<String> errors) {
        super(status, errors);
    }

    public ValidationException(Status status) {
        super(status);
    }
}
