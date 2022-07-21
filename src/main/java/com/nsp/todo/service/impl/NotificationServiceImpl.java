package com.nsp.todo.service.impl;

import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.NotificationException;
import com.nsp.todo.exception.UserException;
import com.nsp.todo.mapper.MapperService;
import com.nsp.todo.model.Notification;
import com.nsp.todo.model.User;
import com.nsp.todo.model.dto.NotificationDto;
import com.nsp.todo.model.dto.update.UpdateNotificationDto;
import com.nsp.todo.model.response.NotificationResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.repository.NotificationRepo;
import com.nsp.todo.repository.UserRepo;
import com.nsp.todo.service.NotificationService;
import com.nsp.todo.validation.ValidatorService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final MapperService mapperService;
    private ValidatorService validatorService;
    @Override
    public ResponseModel<List<NotificationResponse>> getAllNotification() {
        List<Notification> notifications = notificationRepo.findAll();
        if(notifications.isEmpty())
            return ResponseModel.<List<NotificationResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.NOTIFICATION_NOT_FOUND))
                    .build();
        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(n->{
                    NotificationResponse response = modelMapper.map(n,NotificationResponse.class);
                    response.setSender(n.getSender().getName() + " " + n.getSender().getSurname());
                    return response;
                }).collect(Collectors.toList());
        return ResponseModel.<List<NotificationResponse>>builder()
                .model(notificationResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<NotificationResponse> getNotificationById(Long id) {
        Optional<Notification> notification = notificationRepo.findById(id);
        if(!notification.isPresent())
            return ResponseModel.<NotificationResponse>builder()
                    .response(Response.getNotFound(Status.NOTIFICATION_NOT_FOUND))
                    .build();
        NotificationResponse notificationResponse = modelMapper.map(notification.get(), NotificationResponse.class);
        notificationResponse.setSender(notification.get().getSender().getName()+" "+notification.get().getSender().getSurname());
        return ResponseModel.<NotificationResponse>builder()
                .model(notificationResponse)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<List<NotificationResponse>> getUserNotifications(Long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if(!optionalUser.isPresent())
            return ResponseModel.<List<NotificationResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.USER_NOT_FOUND))
                    .build();
        if(optionalUser.get().getNotifications().isEmpty())
            return ResponseModel.<List<NotificationResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.NOTIFICATION_NOT_FOUND))
                    .build();
        List<NotificationResponse> notificationResponses = optionalUser.get().getNotifications().stream()
                .map(n->{
                    NotificationResponse response = modelMapper.map(n,NotificationResponse.class);
                    response.setSender(n.getSender().getName() + " " + n.getSender().getSurname());
                    return response;
                }).collect(Collectors.toList());
        return ResponseModel.<List<NotificationResponse>>builder()
                .model(notificationResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Transactional
    @SneakyThrows
    @Override
    public void addNotification(NotificationDto notificationDto) {
        Notification notification = modelMapper.map(notificationDto,Notification.class);
        if(!userRepo.existsById(notification.getSender().getId()))
            throw new UserException(Status.USER_NOT_FOUND);
        notification.getUsers().stream().forEach(u->{
            if(!userRepo.existsById(u.getId())) {
                try {
                    throw new UserException(Status.USER_NOT_FOUND);
                } catch (UserException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        notificationRepo.save(notification);
    }

    @Transactional
    @SneakyThrows
    @Override
    public void updateNotification(UpdateNotificationDto notificationDto, Long id) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(()->new NotificationException(Status.NOTIFICATION_NOT_FOUND));
        if(!notificationDto.getUsers().isEmpty())
            notificationDto.getUsers().stream().forEach(u->{
                if(!userRepo.existsById(u.getId())) {
                    try {
                        throw new UserException(Status.USER_NOT_FOUND);
                    } catch (UserException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        Notification updatedNotification = mapperService.updateObject(notificationDto,notification);
        updatedNotification.setId(notification.getId());
        updatedNotification.setUpdatedDate(new Date());
        notificationRepo.save(updatedNotification);
    }

    @Transactional
    @SneakyThrows
    @Override
    public void deleteNotification(Long id) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(()->new NotificationException(Status.NOTIFICATION_NOT_FOUND));
        notification.getUsers().forEach(u->u.getNotifications().remove(notification));
        userRepo.saveAll(notification.getUsers());
        notificationRepo.delete(notification);
    }
}
