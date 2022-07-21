package com.nsp.todo.exception;

import com.nsp.todo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseException extends Exception{
    private Status status;
    private Set<String> errors;

    public BaseException(Status status) {
        this.status = status;
    }
}
