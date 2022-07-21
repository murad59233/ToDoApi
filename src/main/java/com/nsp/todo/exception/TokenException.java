package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

public class TokenException extends BaseException{
    public TokenException(Status status) {
        super(status);
    }
}
