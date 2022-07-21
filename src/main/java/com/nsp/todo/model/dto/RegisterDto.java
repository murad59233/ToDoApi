package com.nsp.todo.model.dto;

import com.nsp.todo.model.Country;
import com.nsp.todo.model.dto.simple.SimpleLanguageDto;
import com.nsp.todo.model.dto.simple.SimpleTechnologyDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class RegisterDto {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String phone;
    private String password;
    private String github;
    private Country country;
    private Set<SimpleLanguageDto> languages;
    private Set<SimpleTechnologyDto> technology;
}
