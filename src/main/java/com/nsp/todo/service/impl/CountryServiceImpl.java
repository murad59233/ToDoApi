package com.nsp.todo.service.impl;

import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.CountryException;
import com.nsp.todo.mapper.MapperService;
import com.nsp.todo.model.Address;
import com.nsp.todo.model.Country;
import com.nsp.todo.model.dto.CountryDto;
import com.nsp.todo.model.response.CountryResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.repository.AddressRepo;
import com.nsp.todo.repository.CityRepo;
import com.nsp.todo.repository.CountryRepo;
import com.nsp.todo.service.CityService;
import com.nsp.todo.service.CountryService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepo countryRepo;
    private final MapperService mapperService;
    private final ModelMapper modelMapper;
    private final AddressRepo addressRepo;
    private final CityService cityService;

    @Override
    public ResponseModel<List<CountryResponse>> getAllCountry() {
        List<Country> countries = countryRepo.findAll();
        if(countries.isEmpty())
            return ResponseModel.<List<CountryResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.COUNTRY_NOT_FOUND))
                    .build();
        List<CountryResponse> countryResponses = countries.stream()
                .map(c->modelMapper.map(c,CountryResponse.class))
                .collect(Collectors.toList());
        return ResponseModel.<List<CountryResponse>>builder()
                .model(countryResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<CountryResponse> getCountryById(Long id) {
        Optional<Country> countryOptional = countryRepo.findById(id);
        if(!countryOptional.isPresent())
            return ResponseModel.<CountryResponse>builder()
                    .response(Response.getNotFound(Status.COUNTRY_NOT_FOUND))
                    .build();
        CountryResponse countryResponse = modelMapper.map(countryOptional.get(),CountryResponse.class);
        return ResponseModel.<CountryResponse>builder()
                .model(countryResponse)
                .response(Response.getSuccess())
                .build();
    }

    @Transactional
    @Override
    public void addCountry(CountryDto countryDto) {
        Country country = modelMapper.map(countryDto,Country.class);
        countryRepo.save(country);
    }

    @Transactional
    @SneakyThrows
    @Override
    public void updateCountry(CountryDto countryDto,Long id) {
        Country country = countryRepo.findById(id)
                .orElseThrow(()->new CountryException(Status.COUNTRY_NOT_FOUND));
        Country updatedCountry = mapperService.updateObject(countryDto,country);
        updatedCountry.setId(id);
        updatedCountry.setUpdatedDate(new Date());
        countryRepo.save(updatedCountry);
    }

    @Transactional
    @SneakyThrows
    @Override
    public void deleteCountry(Long id) {
        Country country = countryRepo.findById(id)
                .orElseThrow(()->new CountryException(Status.COUNTRY_NOT_FOUND));
        List<Address> addresses = addressRepo.findAll();
        addresses.stream().forEach(a->{
            if(a.getCountry().getId()==country.getId())
                try {
                    throw new CountryException(Status.COUNTRY_ALREADY_HAS_BEEN_USED);
                } catch (CountryException e) {
                    throw new RuntimeException(e);
                }
        });
        country.getCities().forEach(c->cityService.deleteCity(c.getId()));
        countryRepo.delete(country);
    }
}
