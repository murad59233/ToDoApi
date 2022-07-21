package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

public class TechnologyException extends BaseException{
    public TechnologyException(Status status) {
        super(status);
    }
}
