package com.nsp.todo.service;

import com.nsp.todo.model.dto.LanguageDto;
import com.nsp.todo.model.response.LanguageResponse;
import com.nsp.todo.model.response.ResponseModel;

import java.util.List;

public interface LanguageService {
    ResponseModel<List<LanguageResponse>> getAllLanguage();

    ResponseModel<LanguageResponse> getLanguageById(Long id);

    void addLanguage(LanguageDto languageDto);

    void updateLanguage(LanguageDto languageDto, Long id);

    void deleteLanguage(Long id);
}
