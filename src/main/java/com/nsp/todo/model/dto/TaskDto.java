package com.nsp.todo.model.dto;

import com.nsp.todo.model.dto.simple.SimpleUserDto;
import com.nsp.todo.validation.group.AdvancedInfo;
import com.nsp.todo.validation.group.BasicInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class TaskDto {
//    @NotNull(groups = {AdvancedInfo.class})
//    @NotBlank(groups = {AdvancedInfo.class})
//    @Size(min = 3,groups = {AdvancedInfo.class})
//    private String title;
    @NotNull(groups = {AdvancedInfo.class})
    @NotBlank(groups = {AdvancedInfo.class})
    @Size(min = 3,groups = {AdvancedInfo.class})
    private String description;
    @NotNull(groups = {BasicInfo.class})
    @NotBlank(groups = {BasicInfo.class})
    @Size(min = 3,groups = {BasicInfo.class})
    private String path;
    @NotNull(groups = {AdvancedInfo.class})
    private Date deadline;
    @NotNull(groups = {AdvancedInfo.class})
    private Set<SimpleUserDto> users;
}
