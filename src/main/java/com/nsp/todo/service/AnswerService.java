package com.nsp.todo.service;

import com.nsp.todo.model.dto.update.UpdateAnswerDto;
import com.nsp.todo.model.response.AnswerResponse;
import com.nsp.todo.model.response.ResponseModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnswerService {
    ResponseModel<List<AnswerResponse>> getAllAnswer();

    ResponseModel<AnswerResponse> getAnswerById(Long id);

    ResponseModel<List<AnswerResponse>> getAllAnswerByUserId(Long userId);

    void uploadAnswer(String answer, MultipartFile multipartFile);

    String uploadAnswer(MultipartFile multipartFile);

    void updateAnswer(UpdateAnswerDto answerDto, Long id);

    void deleteAnswer(Long id);

    void deleteAnswerFromFolder(String path);
}
