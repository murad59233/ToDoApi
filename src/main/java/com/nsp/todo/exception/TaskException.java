package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

public class TaskException extends BaseException{
    public TaskException(Status status) {
        super(status);
    }
}
