package com.nsp.todo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.TaskException;
import com.nsp.todo.exception.UserException;
import com.nsp.todo.mapper.MapperService;
import com.nsp.todo.model.Task;
import com.nsp.todo.model.User;
import com.nsp.todo.model.dto.TaskDto;
import com.nsp.todo.model.dto.update.UpdateTaskDto;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.model.response.TaskResponse;
import com.nsp.todo.repository.TaskRepo;
import com.nsp.todo.repository.UserRepo;
import com.nsp.todo.service.AnswerService;
import com.nsp.todo.service.TaskService;
import com.nsp.todo.validation.ValidatorService;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final ObjectMapper objectMapper;
    private final ValidatorService validatorService;
    private final MapperService mapperService;
    private final AnswerService answerService;

    public TaskServiceImpl(TaskRepo taskRepo, ModelMapper modelMapper, UserRepo userRepo, ObjectMapper objectMapper, ValidatorService validatorService, MapperService mapperService, AnswerService answerService) {
        this.taskRepo = taskRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.objectMapper = objectMapper;
        this.validatorService = validatorService;
        this.mapperService = mapperService;
        this.answerService = answerService;
    }

    @Value("${rootPath}")
    private String rootPath;
    @Value("${taskPath}")
    private String taskPath;
    @Value("${domain}")
    private String domain;
    @Override
    public ResponseModel<List<TaskResponse>> getAllTask() {
        List<Task> tasks = taskRepo.findAll();
        if(tasks.isEmpty())
            return ResponseModel.<List<TaskResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.TASK_NOT_FOUND))
                    .build();
        List<TaskResponse> taskResponses = tasks.stream()
                .map(t->modelMapper.map(t,TaskResponse.class))
                .collect(Collectors.toList());
        return ResponseModel.<List<TaskResponse>>builder()
                .model(taskResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<TaskResponse> getTaskById(Long id) {
        Optional<Task> taskOptional = taskRepo.findById(id);
        if(!taskOptional.isPresent())
            return ResponseModel.<TaskResponse>builder()
                    .response(Response.getNotFound(Status.TASK_NOT_FOUND))
                    .build();
        TaskResponse taskResponse = modelMapper.map(taskOptional.get(),TaskResponse.class);
        return ResponseModel.<TaskResponse>builder()
                .model(taskResponse)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<List<TaskResponse>> getTaskByUserId(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if(!userOptional.isPresent())
            return ResponseModel.<List<TaskResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.USER_NOT_FOUND))
                    .build();
        if(userOptional.get().getTasks().isEmpty())
            return ResponseModel.<List<TaskResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.TASK_NOT_FOUND))
                    .build();
        List<TaskResponse> taskResponses = userOptional.get().getTasks().stream()
                .map(t->modelMapper.map(t,TaskResponse.class))
                .collect(Collectors.toList());
        return ResponseModel.<List<TaskResponse>>builder()
                .model(taskResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Transactional
    @SneakyThrows
    @Override
    public void addTask(String task, MultipartFile multipartFile) {
        TaskDto taskDto = objectMapper.readValue(task,TaskDto.class);
        validatorService.validate(taskDto, AdvancedInfo.class);
        Task newTask = modelMapper.map(taskDto,Task.class);
        if(taskDto.getUsers().isEmpty())
            throw new UserException(Status.USER_NOT_FOUND);
        Set<User> users = taskDto.getUsers().stream().map(u->{
            if(!userRepo.existsById(u.getId()))
                try {
                    throw new UserException(Status.USER_NOT_FOUND);
                } catch (UserException e) {
                    throw new RuntimeException(e);
                }
            return modelMapper.map(u, User.class);
        }).collect(Collectors.toSet());
        newTask.setUsers(users);
        String path = uploadTask(multipartFile);
        newTask.setPath(path);
        taskRepo.save(newTask);
    }

    @SneakyThrows
    @Override
    public String uploadTask(MultipartFile multipartFile) {
        byte[] task = IOUtils.toByteArray(multipartFile.getInputStream());
        String taskName = multipartFile.getOriginalFilename();
        String filePath = rootPath + "task/" + taskName;
        File file = new File(filePath);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(task);
        fos.close();
        return domain+taskPath+multipartFile.getOriginalFilename();
    }

    @Transactional
    @SneakyThrows
    @Override
    public void updateTask(UpdateTaskDto updateTaskDto, Long id) {
       Task task = taskRepo.findById(id)
               .orElseThrow(()->new TaskException(Status.TASK_NOT_FOUND));
       Task updatedTask = mapperService.updateObject(updateTaskDto,task);
       if(updateTaskDto.getUsers()!=null){
           if(!updatedTask.getUsers().isEmpty()) {
               Set<User> users = updateTaskDto.getUsers().stream()
                       .map(u -> {
                           if (!userRepo.existsById(u.getId()))
                               try {
                                   throw new UserException(Status.USER_NOT_FOUND);
                               } catch (UserException e) {
                                   throw new RuntimeException(e);
                               }
                           return modelMapper.map(u, User.class);
                       }).collect(Collectors.toSet());
               updatedTask.setUsers(users);
           }
       }
       updatedTask.setId(task.getId());
       updatedTask.setUpdatedDate(new Date());
       taskRepo.save(updatedTask);
    }

    @Transactional
    @SneakyThrows
    @Override
    public void deleteTask(Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(()->new TaskException(Status.TASK_NOT_FOUND));
        task.getUsers().forEach(u->u.getTasks().remove(task));
        userRepo.saveAll(task.getUsers());
        deleteTaskFromFolder(task.getPath());
        task.getAnswers().forEach(a-> answerService.deleteAnswerFromFolder(a.getPath()));
        taskRepo.delete(task);
    }

    @Override
    public void deleteTaskFromFolder(String path) {
        String originalPath = rootPath + path.substring(domain.length()+"upload/".length());
        File file = new File(originalPath);
        if(file.exists())
            file.delete();
    }


}
