package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

public class AnswerException extends BaseException{
    public AnswerException(Status status) {
        super(status);
    }
}
