package com.nsp.todo.model.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {
    private Long id;
    private String address;
    private String zipCode;
    private CountryResponse country;
    private CityResponse city;
}
