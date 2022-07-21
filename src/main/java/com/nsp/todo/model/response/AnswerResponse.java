package com.nsp.todo.model.response;

import com.nsp.todo.model.Task;
import com.nsp.todo.model.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AnswerResponse {
    private Long id;
    private String description;
    private String path;
}
