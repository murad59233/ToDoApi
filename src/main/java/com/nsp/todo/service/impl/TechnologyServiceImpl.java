package com.nsp.todo.service.impl;

import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.TechnologyException;
import com.nsp.todo.mapper.MapperService;
import com.nsp.todo.model.Language;
import com.nsp.todo.model.Technology;
import com.nsp.todo.model.dto.TechnologyDto;
import com.nsp.todo.model.response.LanguageResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.model.response.TechnologyResponse;
import com.nsp.todo.repository.TechnologyRepo;
import com.nsp.todo.repository.UserRepo;
import com.nsp.todo.service.TechnologyService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TechnologyServiceImpl implements TechnologyService {

    private final TechnologyRepo technologyRepo;
    private final ModelMapper modelMapper;
    private final MapperService mapperService;
    @Override
    public ResponseModel<List<TechnologyResponse>> getAllTechnology() {
        List<Technology> technologies = technologyRepo.findAll();
        if(technologies.isEmpty())
            return ResponseModel.<List<TechnologyResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.TECHNOLOGY_NOT_FOUND))
                    .build();
        List<TechnologyResponse> technologyResponses = technologies.stream()
                .map(l->modelMapper.map(l, TechnologyResponse.class))
                .collect(Collectors.toList());
        return ResponseModel.<List<TechnologyResponse>>builder()
                .model(technologyResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<TechnologyResponse> getTechnologyById(Long id) {
        Optional<Technology> optionalTechnology = technologyRepo.findById(id);
        if(!optionalTechnology.isPresent())
            return ResponseModel.<TechnologyResponse>builder()
                    .response(Response.getNotFound(Status.TECHNOLOGY_NOT_FOUND))
                    .build();
        TechnologyResponse technologyResponse = modelMapper.map(optionalTechnology.get(), TechnologyResponse.class);
        return ResponseModel.<TechnologyResponse>builder()
                .model(technologyResponse)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public void addTechnology(TechnologyDto technologyDto) {
        Technology technology = modelMapper.map(technologyDto,Technology.class);
        technologyRepo.save(technology);
    }

    @SneakyThrows
    @Override
    public void updateTechnology(TechnologyDto technologyDto, Long id) {
        Technology technology = technologyRepo.findById(id)
                .orElseThrow(()->new TechnologyException(Status.TECHNOLOGY_NOT_FOUND));
        Technology updatedTechnology = mapperService.updateObject(technologyDto,technology);
        updatedTechnology.setId(id);
        updatedTechnology.setUpdatedDate(new Date());
        technologyRepo.save(updatedTechnology);
    }

    @SneakyThrows
    @Override
    public void deleteTechnology(Long id) {
        Technology technology = technologyRepo.findById(id)
                .orElseThrow(()->new TechnologyException(Status.TECHNOLOGY_NOT_FOUND));
        if(!technology.getUsers().isEmpty())
            throw new TechnologyException(Status.TECHNOLOGY_HAS_BEEN_USED);
        technologyRepo.delete(technology);
    }


}
