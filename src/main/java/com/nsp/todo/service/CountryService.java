package com.nsp.todo.service;

import com.nsp.todo.model.dto.CountryDto;
import com.nsp.todo.model.response.CountryResponse;
import com.nsp.todo.model.response.ResponseModel;

import java.util.List;

public interface CountryService {
    ResponseModel<List<CountryResponse>> getAllCountry();

    ResponseModel<CountryResponse> getCountryById(Long id);

    void addCountry(CountryDto countryDto);

    void updateCountry(CountryDto countryDto,Long id);

    void deleteCountry(Long id);
}
