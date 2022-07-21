package com.nsp.todo.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class NotificationResponse{
    private Long id;
    private String title;
    private String notification;
    private String sender;
    private Date createdDate;
}
