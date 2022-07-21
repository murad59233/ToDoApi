package com.nsp.todo.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    //private String title;
    private String description;
    private String path;
    private Date deadline;
}
