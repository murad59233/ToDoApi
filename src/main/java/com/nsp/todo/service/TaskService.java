package com.nsp.todo.service;

import com.nsp.todo.model.dto.update.UpdateTaskDto;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.model.response.TaskResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskService {
    ResponseModel<List<TaskResponse>> getAllTask();

    ResponseModel<TaskResponse> getTaskById(Long id);

    ResponseModel<List<TaskResponse>> getTaskByUserId(Long userId);

    void addTask(String task, MultipartFile multipartFile);

    String uploadTask(MultipartFile multipartFile);

    void updateTask(UpdateTaskDto updateTaskDto, Long id);

    void deleteTask(Long id);

    void deleteTaskFromFolder(String path);
}
