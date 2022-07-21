package com.nsp.todo.model.dto;

import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LanguageDto {
    @NotNull(groups = {AdvancedInfo.class})
    @NotBlank(groups = {AdvancedInfo.class})
    private String language;
}
