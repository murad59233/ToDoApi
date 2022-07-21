package com.nsp.todo.controller;

import com.nsp.todo.model.dto.update.UpdateAnswerDto;
import com.nsp.todo.model.response.AnswerResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseContainer;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.service.AnswerService;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseModel<List<AnswerResponse>> getAllAnswer(){
        return answerService.getAllAnswer();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<AnswerResponse> getAnswerById(@PathVariable("id") Long id){
        return answerService.getAnswerById(id);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<List<AnswerResponse>> getAllAnswerByUserId(@PathVariable("userId") Long userId){
        return answerService.getAllAnswerByUserId(userId);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseContainer uploadAnswer(@RequestPart("task") String answer, @RequestPart("file") MultipartFile multipartFile){
        answerService.uploadAnswer(answer,multipartFile);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseContainer updateAnswer(@PathVariable("id") Long id,@Validated(AdvancedInfo.class) @RequestBody UpdateAnswerDto answerDto){
        answerService.updateAnswer(answerDto,id);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseContainer deleteAnswer(@PathVariable("id") Long id){
        answerService.deleteAnswer(id);
        return ResponseContainer.ok(Response.getSuccess());
    }


}
