package com.nsp.todo.service;

import com.nsp.todo.model.dto.CityDto;
import com.nsp.todo.model.dto.update.UpdateCityDto;
import com.nsp.todo.model.response.CityResponse;
import com.nsp.todo.model.response.ResponseModel;

import java.util.List;

public interface CityService {
    ResponseModel<List<CityResponse>> getAllCity();

    ResponseModel<List<CityResponse>> getAllCityByCountryId(Long countryId);

    ResponseModel<CityResponse> getCityById(Long id);

    void addCity(CityDto cityDto);

    void updateCity(UpdateCityDto cityDto, Long id);

    void deleteCity(Long id);
}
