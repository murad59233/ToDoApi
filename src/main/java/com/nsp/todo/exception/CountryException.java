package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

public class CountryException extends BaseException{
    public CountryException(Status status) {
        super(status);
    }
}
