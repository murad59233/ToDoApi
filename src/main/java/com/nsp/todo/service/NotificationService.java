package com.nsp.todo.service;

import com.nsp.todo.model.dto.NotificationDto;
import com.nsp.todo.model.dto.update.UpdateNotificationDto;
import com.nsp.todo.model.response.NotificationResponse;
import com.nsp.todo.model.response.ResponseModel;

import java.util.List;

public interface NotificationService {
    ResponseModel<List<NotificationResponse>> getAllNotification();

    ResponseModel<NotificationResponse> getNotificationById(Long id);

    ResponseModel<List<NotificationResponse>> getUserNotifications(Long userId);

    void addNotification(NotificationDto notificationDto);

    void updateNotification(UpdateNotificationDto notificationDto, Long id);

    void deleteNotification(Long id);
}
