package com.nsp.todo.model.response;

import com.nsp.todo.enums.Role;
import com.nsp.todo.model.Address;
import com.nsp.todo.model.Cv;
import com.nsp.todo.model.Language;
import com.nsp.todo.model.Technology;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String phone;
    private String github;
    private CvResponse cv;
    private AddressResponse address;
    private Set<LanguageResponse> languages;
    private Set<TechnologyResponse> technology;

}
