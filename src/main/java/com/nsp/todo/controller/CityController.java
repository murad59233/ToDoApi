package com.nsp.todo.controller;


import com.nsp.todo.model.dto.CityDto;
import com.nsp.todo.model.dto.update.UpdateCityDto;
import com.nsp.todo.model.response.CityResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseContainer;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.service.CityService;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/city")
public class CityController {

    private final CityService cityService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<List<CityResponse>> getAllCity(){
        return cityService.getAllCity();
    }

    @GetMapping("/country/{countryId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    private ResponseModel<List<CityResponse>> getAllCityByCountryId(@PathVariable("countryId") Long countryId){
        return cityService.getAllCityByCountryId(countryId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<CityResponse> getCityById(@PathVariable("id") Long id){
        return cityService.getCityById(id);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer addCity(@Validated(AdvancedInfo.class) @RequestBody CityDto cityDto){
        cityService.addCity(cityDto);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer updateCity(@PathVariable("id") Long id,@Validated(AdvancedInfo.class) @RequestBody UpdateCityDto cityDto){
        cityService.updateCity(cityDto, id);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer deleteCity(@PathVariable("id") Long id){
        cityService.deleteCity(id);
        return ResponseContainer.ok(Response.getSuccess());
    }
}
