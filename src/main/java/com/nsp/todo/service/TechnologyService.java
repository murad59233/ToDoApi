package com.nsp.todo.service;

import com.nsp.todo.model.dto.TechnologyDto;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.model.response.TechnologyResponse;

import java.util.List;

public interface TechnologyService {
    ResponseModel<List<TechnologyResponse>> getAllTechnology();

    ResponseModel<TechnologyResponse> getTechnologyById(Long id);

    void addTechnology(TechnologyDto technologyDto);

    void updateTechnology(TechnologyDto technologyDto, Long id);

    void deleteTechnology(Long id);
}
