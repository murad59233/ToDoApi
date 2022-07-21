package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

public class NotificationException extends BaseException{
    public NotificationException(Status status) {
        super(status);
    }
}
