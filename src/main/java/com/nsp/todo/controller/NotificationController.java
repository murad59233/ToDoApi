package com.nsp.todo.controller;

import com.nsp.todo.model.dto.NotificationDto;
import com.nsp.todo.model.dto.update.UpdateNotificationDto;
import com.nsp.todo.model.response.NotificationResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseContainer;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.service.NotificationService;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseModel<List<NotificationResponse>> getAllNotification(){
        return notificationService.getAllNotification();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<NotificationResponse> getNotificationById(@PathVariable("id") Long id){
        return notificationService.getNotificationById(id);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseModel<List<NotificationResponse>> getUserNotifications(@PathVariable("userId") Long userId){
        return notificationService.getUserNotifications(userId);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer addNotification(@Validated(AdvancedInfo.class) @RequestBody NotificationDto notificationDto){
        notificationService.addNotification(notificationDto);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer updateNotification(@PathVariable("id") Long id,@RequestBody UpdateNotificationDto notificationDto){
        notificationService.updateNotification(notificationDto, id);
        return ResponseContainer.ok(Response.getSuccess());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseContainer deleteNotification(@PathVariable("id") Long id){
        notificationService.deleteNotification(id);
        return ResponseContainer.ok(Response.getSuccess());
    }

}
