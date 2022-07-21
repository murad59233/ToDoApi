package com.nsp.todo.service.impl;

import com.nsp.todo.service.MailService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@AllArgsConstructor
@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;


    @SneakyThrows
    @Async
    @Override
    public void sendMail(String from, String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body,true);
        mailSender.send(message);
    }
}
