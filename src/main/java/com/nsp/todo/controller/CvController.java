package com.nsp.todo.controller;

import com.nsp.todo.model.response.CvResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseContainer;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.service.CvService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/cv")
public class CvController {

    private final CvService cvService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseModel<List<CvResponse>> getAllCv(){
        return cvService.getAllCv();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<CvResponse> getCvById(@PathVariable("id") Long id){
        return cvService.getCvById(id);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<CvResponse> getCvByUserId(@PathVariable("userId") Long userId){
        return cvService.getCvByUserId(userId);
    }

    @PutMapping(value = "/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseContainer updateCv(@PathVariable("id") Long id, @RequestPart("file")MultipartFile multipartFile){
        cvService.updateCv(multipartFile,id);
        return ResponseContainer.ok(Response.getSuccess());
    }

}
