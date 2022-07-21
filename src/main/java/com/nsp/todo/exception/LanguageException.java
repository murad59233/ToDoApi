package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

public class LanguageException extends BaseException{
    public LanguageException(Status status) {
        super(status);
    }
}
