package com.nsp.todo.controller;

import com.nsp.todo.model.dto.CountryDto;
import com.nsp.todo.model.response.CountryResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseContainer;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.service.CountryService;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/country")
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<List<CountryResponse>> getAllCountry(){
        return countryService.getAllCountry();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<CountryResponse> getCountryById(@PathVariable("id") Long id){
        return countryService.getCountryById(id);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer addCountry(@Validated(AdvancedInfo.class) @RequestBody CountryDto countryDto){
        countryService.addCountry(countryDto);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer updateCountry(@Validated(AdvancedInfo.class) @RequestBody CountryDto countryDto,@PathVariable("id") Long id){
        countryService.updateCountry(countryDto, id);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer deleteCountry(@PathVariable("id") Long id){
        countryService.deleteCountry(id);
        return ResponseContainer.ok(Response.getSuccess());
    }

}
