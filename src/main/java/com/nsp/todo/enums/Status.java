package com.nsp.todo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum Status {
    SUCCESS(100,"Operation is successful"),
    USER_NOT_FOUND(101,"User not found"),
    USER_ALREADY_EXISTS(102,"User already exists"),
    EMAIL_SENT_ERROR(104,"Email not sent"),
    USERNAME_EXISTS(103,"Username has already taken"),
    EMAIL_EXISTS(105,"Email has already taken"),
    VALIDATION_ERROR(106,"Parameter is not valid"),
    TOKEN_HAS_EXPIRED(107,"Token has expired"),
    TOKEN_NOT_FOUND(108,"Token not found"),
    CV_IS_EMPTY(109,"File cannot be empty"),
    CV_NOT_FOUND(110,"Resume not found"),
    LANGUAGE_NOT_FOUND(111,"Language not found"),
    TECHNOLOGY_NOT_FOUND(112,"Technology not found"),
    NOTIFICATION_NOT_FOUND(113,"Notification not found"),
    TASK_NOT_FOUND(114, "Task not found"),
    TASK_FILE_IS_EMPTY(115,"Task file is empty"),
    TASK_ANSWER_NOT_FOUND(116,"Task answer not found"),
    TASK_ANSWER_FILE_NOT_FOUND(117,"Task answer file not found"),
    COUNTRY_NOT_FOUND(118, "Country not found"),
    CITY_NOT_FOUND(119, "City not found"),
    CITY_NOT_EXIST_IN_COUNTRY(120, "Country doesn't have this city"),
    CITY_HAS_ALREADY_BEEN_USED(121,"This city already has been used in address" ),
    COUNTRY_ALREADY_HAS_BEEN_USED(122,"This country already has been used in address" ),
    LANGUAGE_HAS_BEEN_USED(123, "Language has already been used"),
    TECHNOLOGY_HAS_BEEN_USED(124, "Technology has already been used");
    private Integer code;
    private String message;
}
