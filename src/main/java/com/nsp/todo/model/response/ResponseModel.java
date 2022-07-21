package com.nsp.todo.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel<T> {
    private T model;
    private Response response;
   
}
