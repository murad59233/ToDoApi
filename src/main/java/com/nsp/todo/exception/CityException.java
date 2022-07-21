package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;

public class CityException extends BaseException{
    public CityException(Status status) {
        super(status);
    }
}
