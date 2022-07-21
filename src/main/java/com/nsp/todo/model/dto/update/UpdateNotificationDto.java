package com.nsp.todo.model.dto.update;

import com.nsp.todo.model.dto.simple.SimpleUserDto;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UpdateNotificationDto {
    @NotNull(groups = {AdvancedInfo.class})
    @NotBlank(groups = {AdvancedInfo.class})
    private String title;
    @NotNull(groups = {AdvancedInfo.class})
    @NotBlank(groups = {AdvancedInfo.class})
    private String notification;
    @NotNull(groups = {AdvancedInfo.class})
    private Set<SimpleUserDto> users = new HashSet<>();
}
