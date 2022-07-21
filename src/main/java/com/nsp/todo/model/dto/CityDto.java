package com.nsp.todo.model.dto;

import com.nsp.todo.model.dto.simple.SimpleCountryDto;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CityDto {
    @NotNull(groups = {AdvancedInfo.class})
    @NotBlank(groups = {AdvancedInfo.class})
    @Size(min = 3,groups = {AdvancedInfo.class})
    private String cityName;
    @NotNull(groups = {AdvancedInfo.class})
    private SimpleCountryDto country;
}
