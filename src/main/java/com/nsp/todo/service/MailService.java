package com.nsp.todo.service;

import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;

public interface MailService {
    void sendMail(String from,String to,String subject,String body);
}
