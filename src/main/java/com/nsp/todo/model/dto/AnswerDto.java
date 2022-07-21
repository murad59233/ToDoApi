package com.nsp.todo.model.dto;

import com.nsp.todo.model.dto.simple.SimpleTaskDto;
import com.nsp.todo.model.dto.simple.SimpleUserDto;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AnswerDto {
    @NotNull(groups = {AdvancedInfo.class})
    private String description;
    @NotNull(groups = {AdvancedInfo.class})
    private SimpleTaskDto task;
    @NotNull(groups = {AdvancedInfo.class})
    private SimpleUserDto user;
}
