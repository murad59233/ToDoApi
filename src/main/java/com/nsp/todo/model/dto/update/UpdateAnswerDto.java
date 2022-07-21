package com.nsp.todo.model.dto.update;

import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateAnswerDto {
    @NotNull(groups = {AdvancedInfo.class})
    private String description;
}
