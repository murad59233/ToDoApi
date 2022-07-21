package com.nsp.todo.model.dto;

import com.nsp.todo.model.dto.simple.SimpleCityDto;
import com.nsp.todo.model.dto.simple.SimpleCountryDto;
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
public class AddressDto {
    @NotNull(groups = {AdvancedInfo.class})
    @NotBlank(groups = {AdvancedInfo.class})
    @Size(min = 3, groups = {AdvancedInfo.class})
    private String address;
    @NotNull(groups = {AdvancedInfo.class})
    @NotBlank(groups = {AdvancedInfo.class})
    @Size(min = 3, groups = {AdvancedInfo.class})
    private String zipCode;
    @NotNull(groups = {AdvancedInfo.class})
    private SimpleCountryDto country;
    @NotNull(groups = {AdvancedInfo.class})
    private SimpleCityDto city;
}
