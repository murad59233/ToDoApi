package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

public class UserException extends BaseException{
    public UserException(Status status) {
        super(status);
    }

}
