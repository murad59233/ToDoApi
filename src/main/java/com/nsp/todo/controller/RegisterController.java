package com.nsp.todo.controller;

import com.nsp.todo.model.response.ResponseContainer;
import com.nsp.todo.service.impl.UserDetailsServiceImpl;
import com.nsp.todo.model.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
public class RegisterController {

    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping(value = "/register" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseContainer register(@RequestPart("user") String user, @RequestPart("cv") MultipartFile multipartFile){
       userDetailsService.register(user,multipartFile);
        return ResponseContainer.ok(Response.getSuccess());
    }


    @GetMapping("/register/confirm/{token}")
    public ResponseContainer confirmUser(@PathVariable("token") String token){
        userDetailsService.confirmUser(token);
        return ResponseContainer.ok(Response.getSuccess());
    }



}
