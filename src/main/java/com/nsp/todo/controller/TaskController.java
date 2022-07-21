package com.nsp.todo.controller;

import com.nsp.todo.model.dto.update.UpdateTaskDto;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseContainer;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.model.response.TaskResponse;
import com.nsp.todo.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseModel<List<TaskResponse>> getAllTask(){
        return taskService.getAllTask();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<TaskResponse> getTaskById(@PathVariable("id") Long id){
        return taskService.getTaskById(id);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<List<TaskResponse>> getTaskByUserId(@PathVariable("userId") Long userId){
        return taskService.getTaskByUserId(userId);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer addTask(@RequestPart("task") String task, @RequestPart("file") MultipartFile multipartFile){
        taskService.addTask(task,multipartFile);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer updateTask(@RequestBody UpdateTaskDto updateTaskDto, @PathVariable("id") Long id){
        taskService.updateTask(updateTaskDto, id);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer deleteTask(@PathVariable("id") Long id){
        taskService.deleteTask(id);
        return ResponseContainer.ok(Response.getSuccess());
    }
}
