package com.nsp.todo.controller;

import com.nsp.todo.model.dto.TechnologyDto;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseContainer;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.model.response.TechnologyResponse;
import com.nsp.todo.service.TechnologyService;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/technology")
public class TechnologyController {

    private final TechnologyService technologyService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<List<TechnologyResponse>> getAllTechnology(){
        return technologyService.getAllTechnology();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<TechnologyResponse> getTechnologyById(@PathVariable Long id){
        return technologyService.getTechnologyById(id);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer addTechnology(@Validated(AdvancedInfo.class) @RequestBody TechnologyDto technologyDto){
        technologyService.addTechnology(technologyDto);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer updateTechnology(@PathVariable("id") Long id,@Validated(AdvancedInfo.class) @RequestBody TechnologyDto technologyDto){
        technologyService.updateTechnology(technologyDto, id);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer deleteTechnology(@PathVariable("id") Long id){
        technologyService.deleteTechnology(id);
        return ResponseContainer.ok(Response.getSuccess());
    }

}
