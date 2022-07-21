package com.nsp.todo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.AnswerException;
import com.nsp.todo.exception.TaskException;
import com.nsp.todo.exception.UserException;
import com.nsp.todo.mapper.MapperService;
import com.nsp.todo.model.Answer;
import com.nsp.todo.model.User;
import com.nsp.todo.model.dto.AnswerDto;
import com.nsp.todo.model.dto.update.UpdateAnswerDto;
import com.nsp.todo.model.response.AnswerResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.repository.AnswerRepo;
import com.nsp.todo.repository.TaskRepo;
import com.nsp.todo.repository.UserRepo;
import com.nsp.todo.service.AnswerService;
import com.nsp.todo.validation.ValidatorService;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepo answerRepo;
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final ObjectMapper objectMapper;
    private final ValidatorService validatorService;
    private final TaskRepo taskRepo;
    private final MapperService mapperService;

    public AnswerServiceImpl(AnswerRepo answerRepo, ModelMapper modelMapper, UserRepo userRepo, ObjectMapper objectMapper, ValidatorService validatorService, TaskRepo taskRepo, MapperService mapperService) {
        this.answerRepo = answerRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.objectMapper = objectMapper;
        this.validatorService = validatorService;
        this.taskRepo = taskRepo;
        this.mapperService = mapperService;
    }

    @Value("${rootPath}")
    private String rootPath;
    @Value("${answerPath}")
    private String answerPath;
    @Value("${domain}")
    private String domain;
    @Override
    public ResponseModel<List<AnswerResponse>> getAllAnswer() {
        List<Answer> answers = answerRepo.findAll();
        if(answers.isEmpty())
            return ResponseModel.<List<AnswerResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.TASK_ANSWER_NOT_FOUND))
                    .build();
        List<AnswerResponse> answerResponses = answers.stream()
                .map(a->modelMapper.map(a,AnswerResponse.class))
                .collect(Collectors.toList());
        return ResponseModel.<List<AnswerResponse>>builder()
                .model(answerResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<AnswerResponse> getAnswerById(Long id) {
        Optional<Answer> answerOptional = answerRepo.findById(id);
        if(!answerOptional.isPresent())
            return ResponseModel.<AnswerResponse>builder()
                    .response(Response.getNotFound(Status.TASK_ANSWER_NOT_FOUND))
                    .build();
        AnswerResponse answerResponse = modelMapper.map(answerOptional.get(),AnswerResponse.class);
        return ResponseModel.<AnswerResponse>builder()
                .model(answerResponse)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<List<AnswerResponse>> getAllAnswerByUserId(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (!userOptional.isPresent())
            return ResponseModel.<List<AnswerResponse>>builder()
                    .response(Response.getNotFound(Status.USER_NOT_FOUND))
                    .build();
        if(userOptional.get().getAnswers().isEmpty())
            return ResponseModel.<List<AnswerResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.TASK_ANSWER_NOT_FOUND))
                    .build();
        List<AnswerResponse> answerResponses = userOptional.get().getAnswers().stream()
                .map(a->modelMapper.map(a,AnswerResponse.class))
                .collect(Collectors.toList());
        return ResponseModel.<List<AnswerResponse>>builder()
                .model(answerResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Transactional
    @SneakyThrows
    @Override
    public void uploadAnswer(String task, MultipartFile multipartFile) {
        AnswerDto answerDto = objectMapper.readValue(task,AnswerDto.class);
        validatorService.validate(answerDto, AdvancedInfo.class);
        if(!taskRepo.existsById(answerDto.getTask().getId()))
            throw new TaskException(Status.TASK_NOT_FOUND);
        if(!userRepo.existsById(answerDto.getUser().getId()))
            throw new UserException(Status.USER_NOT_FOUND);
        Answer answer = modelMapper.map(answerDto,Answer.class);
        answer.setUser(userRepo.getById(answerDto.getUser().getId()));
        answer.setTask(taskRepo.getById(answerDto.getTask().getId()));
        answer.setPath(uploadAnswer(multipartFile));
        answerRepo.save(answer);
    }


    @SneakyThrows
    @Override
    public String uploadAnswer(MultipartFile multipartFile) {
        byte[] task = IOUtils.toByteArray(multipartFile.getInputStream());
        String answerName = multipartFile.getOriginalFilename();
        String path = rootPath + "answer/" + answerName;
        File file = new File(path);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(task);
        fos.close();
        return domain+answerPath+multipartFile.getOriginalFilename();
    }

    @Transactional
    @SneakyThrows
    @Override
    public void updateAnswer(UpdateAnswerDto answerDto, Long id) {
        Answer answer = answerRepo.findById(id)
                .orElseThrow(()->new AnswerException(Status.TASK_ANSWER_NOT_FOUND));
        Answer updatedAnswer = mapperService.updateObject(answerDto,answer);
        updatedAnswer.setId(answer.getId());
        updatedAnswer.setUpdatedDate(new Date());
        answerRepo.save(updatedAnswer);
    }

    @Transactional
    @SneakyThrows
    @Override
    public void deleteAnswer(Long id) {
        Answer answer = answerRepo.findById(id)
                .orElseThrow(()->new AnswerException(Status.TASK_ANSWER_NOT_FOUND));
        deleteAnswerFromFolder(answer.getPath());
        answerRepo.delete(answer);
    }

    @Override
    public void deleteAnswerFromFolder(String path) {
        String originalPath = rootPath + path.substring(domain.length()+"upload/".length());
        File file = new File(originalPath);
        if(file.exists())
            file.delete();
    }


}
