package com.nsp.todo.service.impl;

import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.CityException;
import com.nsp.todo.exception.CountryException;
import com.nsp.todo.mapper.MapperService;
import com.nsp.todo.model.Address;
import com.nsp.todo.model.City;
import com.nsp.todo.model.Country;
import com.nsp.todo.model.dto.CityDto;
import com.nsp.todo.model.dto.update.UpdateCityDto;
import com.nsp.todo.model.response.CityResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.repository.AddressRepo;
import com.nsp.todo.repository.CityRepo;
import com.nsp.todo.repository.CountryRepo;
import com.nsp.todo.service.CityService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CityServiceImpl implements CityService {

    private final CityRepo cityRepo;
    private final CountryRepo countryRepo;
    private final ModelMapper modelMapper;
    private final MapperService mapperService;
    private final AddressRepo addressRepo;

    @Override
    public ResponseModel<List<CityResponse>> getAllCity() {
        List<City> cities = cityRepo.findAll();
        if(cities.isEmpty())
            return ResponseModel.<List<CityResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.CITY_NOT_FOUND))
                    .build();
        List<CityResponse> cityResponses = cities.stream()
                .map(c->modelMapper.map(c,CityResponse.class))
                .collect(Collectors.toList());
        return ResponseModel.<List<CityResponse>>builder()
                .model(cityResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<List<CityResponse>> getAllCityByCountryId(Long countryId) {
        Optional<Country> country = countryRepo.findById(countryId);
        if(!country.isPresent())
            return ResponseModel.<List<CityResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.COUNTRY_NOT_FOUND))
                    .build();
        if(country.get().getCities().isEmpty())
            return ResponseModel.<List<CityResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.CITY_NOT_FOUND))
                    .build();
        List<CityResponse> cityResponses = country.get().getCities().stream()
                .map(c->modelMapper.map(c,CityResponse.class))
                .collect(Collectors.toList());
        return ResponseModel.<List<CityResponse>>builder()
                .model(cityResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<CityResponse> getCityById(Long id) {
        Optional<City> cityOptional = cityRepo.findById(id);
        if(!cityOptional.isPresent())
            return ResponseModel.<CityResponse>builder()
                    .response(Response.getNotFound(Status.COUNTRY_NOT_FOUND))
                    .build();
        CityResponse cityResponse = modelMapper.map(cityOptional.get(), CityResponse.class);
        return ResponseModel.<CityResponse>builder()
                .model(cityResponse)
                .response(Response.getSuccess())
                .build();
    }

    @SneakyThrows
    @Transactional
    @Override
    public void addCity(CityDto cityDto) {
        City city = modelMapper.map(cityDto, City.class);
        if(!countryRepo.existsById(cityDto.getCountry().getId()))
            throw new CountryException(Status.COUNTRY_NOT_FOUND);
        city.setCountry(countryRepo.getById(cityDto.getCountry().getId()));
        cityRepo.save(city);
    }

    @Transactional
    @SneakyThrows
    @Override
    public void updateCity(UpdateCityDto cityDto, Long id) {
        City city = cityRepo.findById(id)
                .orElseThrow(()->new CityException(Status.CITY_NOT_FOUND));
        City updatedCity = mapperService.updateObject(cityDto,city);
        updatedCity.setId(city.getId());
        updatedCity.setUpdatedDate(new Date());
        cityRepo.save(updatedCity);
    }

    @Transactional
    @SneakyThrows
    @Override
    public void deleteCity(Long id) {
        City city = cityRepo.findById(id)
                .orElseThrow(()->new CityException(Status.CITY_NOT_FOUND));
        List<Address> addresses = addressRepo.findAll();
        addresses.stream().forEach(a->{
            if(a.getCity().getId()==city.getId())
                try {
                    throw new CityException(Status.CITY_HAS_ALREADY_BEEN_USED);
                } catch (CityException e) {
                    throw new RuntimeException(e);
                }
        });
        cityRepo.delete(city);
        cityRepo.delete(city);
    }
}
