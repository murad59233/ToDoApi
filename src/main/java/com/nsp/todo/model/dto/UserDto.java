package com.nsp.todo.model.dto;

import com.nsp.todo.model.dto.simple.SimpleLanguageDto;
import com.nsp.todo.model.dto.simple.SimpleTechnologyDto;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@ToString
public class UserDto {
    @NotBlank(groups = {AdvancedInfo.class})
    @NotNull(groups = {AdvancedInfo.class})
    @Size(min = 3,message = "name size can't less than 3",groups = {AdvancedInfo.class})
    private String name;
    @NotBlank(groups = {AdvancedInfo.class})
    @NotNull(groups = {AdvancedInfo.class})
    @Size(min = 3,message = "surname size can't less than 3",groups = {AdvancedInfo.class})
    private String surname;
    @NotBlank(groups = {AdvancedInfo.class})
    @NotNull(groups = {AdvancedInfo.class})
    @Size(min = 3,message = "username size can't less than 3",groups = {AdvancedInfo.class})
    private String username;
    @NotBlank(groups = {AdvancedInfo.class})
    @NotNull(groups = {AdvancedInfo.class})
    @Size(min = 3,message = "email size can't less than 3",groups = {AdvancedInfo.class})
    private String email;
    @NotBlank(groups = {AdvancedInfo.class})
    @NotNull(groups = {AdvancedInfo.class})
    @Size(min = 3,message = "phone size can't less than 3",groups = {AdvancedInfo.class})
    private String phone;
    @NotBlank(groups = {AdvancedInfo.class})
    @NotNull(groups = {AdvancedInfo.class})
    @Size(min = 3,message = "password size can't less than 3",groups = {AdvancedInfo.class})
    private String password;
    @NotBlank(groups = {AdvancedInfo.class})
    @NotNull(groups = {AdvancedInfo.class})
    @Size(min = 3,message = "github size can't less than 3",groups = {AdvancedInfo.class})
    private String github;
    @NotNull(groups = {AdvancedInfo.class})
    private AddressDto address;
    @NotNull(groups = {AdvancedInfo.class})
    private Set<SimpleLanguageDto> languages;
    @NotNull(groups = {AdvancedInfo.class})
    private Set<SimpleTechnologyDto> technology;
}
