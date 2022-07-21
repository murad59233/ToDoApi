package com.nsp.todo.service.impl;

import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.LanguageException;
import com.nsp.todo.mapper.MapperService;
import com.nsp.todo.model.Language;
import com.nsp.todo.model.dto.LanguageDto;
import com.nsp.todo.model.response.LanguageResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.repository.LanguageRepo;
import com.nsp.todo.repository.UserRepo;
import com.nsp.todo.service.LanguageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepo languageRepo;
    private final ModelMapper modelMapper;
    private final MapperService mapperService;
    private final UserRepo userRepo;
    @Override
    public ResponseModel<List<LanguageResponse>> getAllLanguage() {
        List<Language> languages = languageRepo.findAll();
        if(languages.isEmpty())
            return ResponseModel.<List<LanguageResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.LANGUAGE_NOT_FOUND))
                    .build();
        List<LanguageResponse> languageResponses = languages.stream()
                .map(l->modelMapper.map(l, LanguageResponse.class))
                .collect(Collectors.toList());
        return ResponseModel.<List<LanguageResponse>>builder()
                .model(languageResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<LanguageResponse> getLanguageById(Long id) {
        Optional<Language> optionalLanguage = languageRepo.findById(id);
        if(!optionalLanguage.isPresent())
            return ResponseModel.<LanguageResponse>builder()
                    .response(Response.getNotFound(Status.LANGUAGE_NOT_FOUND))
                    .build();
        LanguageResponse languageResponse = modelMapper.map(optionalLanguage.get(), LanguageResponse.class);
        return ResponseModel.<LanguageResponse>builder()
                .model(languageResponse)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public void addLanguage(LanguageDto languageDto) {
        Language language = modelMapper.map(languageDto, Language.class);
        languageRepo.save(language);
    }

    @SneakyThrows
    @Override
    public void updateLanguage(LanguageDto languageDto, Long id) {
        Language language = languageRepo.findById(id)
                .orElseThrow(()->new LanguageException(Status.LANGUAGE_NOT_FOUND));
        Language updatedLanguage = mapperService.updateObject(languageDto, language);
        updatedLanguage.setId(id);
        updatedLanguage.setUpdatedDate(new Date());
        languageRepo.save(updatedLanguage);
    }

    @SneakyThrows
    @Transactional
    @Override
    public void deleteLanguage(Long id) {
        Language language = languageRepo.findById(id)
                .orElseThrow(()->new LanguageException(Status.LANGUAGE_NOT_FOUND));
        if(!language.getUsers().isEmpty())
            throw new LanguageException(Status.LANGUAGE_HAS_BEEN_USED);
        languageRepo.delete(language);
    }
}
