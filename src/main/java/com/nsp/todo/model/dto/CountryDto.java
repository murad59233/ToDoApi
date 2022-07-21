package com.nsp.todo.model.dto;

import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class CountryDto {
    @NotNull(groups = {AdvancedInfo.class})
    @NotBlank(groups = {AdvancedInfo.class})
    @Size(min = 3,groups = {AdvancedInfo.class})
    private String countryName;
}
