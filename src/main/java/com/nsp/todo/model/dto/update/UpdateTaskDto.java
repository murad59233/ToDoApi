package com.nsp.todo.model.dto.update;

import com.nsp.todo.model.dto.simple.SimpleUserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class UpdateTaskDto {
    private String title;
    private String description;
    private Date deadline;
    private Set<SimpleUserDto> users;
}
