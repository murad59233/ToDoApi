package com.nsp.todo.model.dto;

import com.nsp.todo.model.dto.simple.SimpleUserDto;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class NotificationDto {
    @NotNull(groups = {AdvancedInfo.class})
    @NotBlank(groups = {AdvancedInfo.class})
    private String title;
    @NotNull(groups = {AdvancedInfo.class})
    @NotBlank(groups = {AdvancedInfo.class})
    private String notification;
    @NotNull(groups = {AdvancedInfo.class})
    private SimpleUserDto sender;
    @NotNull(groups = {AdvancedInfo.class})
    private Set<SimpleUserDto> users;
}
