package com.nsp.todo.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseContainer<T> {
    private T response;

    public static <T>  ResponseContainer ok(T t){
        return builder().response(t).build();
    }
}
