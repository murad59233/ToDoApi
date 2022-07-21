package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

public class CvException extends BaseException{
    public CvException(Status status) {
        super(status);
    }
}
