package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

public class AddressException extends BaseException{
    public AddressException(Status status) {
        super(status);
    }
}
