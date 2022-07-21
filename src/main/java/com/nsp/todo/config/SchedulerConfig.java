package com.nsp.todo.config;

import com.nsp.todo.model.Token;
import com.nsp.todo.model.User;
import com.nsp.todo.repository.TokenRepo;
import com.nsp.todo.repository.UserRepo;
import com.nsp.todo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@EnableScheduling
@Configuration
public class SchedulerConfig {

    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final UserService userService;

    @Scheduled(fixedRate = 600000)
    public void deleteDisableUser(){
        List<User> userList = userRepo.findAll();
        userList.stream().forEach(u->{
            if(u.getCreatedDate().before(new Date(System.currentTimeMillis()+10*60000))) {
                userService.deleteDisableUserByEmailOrUsername(u.getUsername(), u.getEmail(), false);
            }
        });
    }

    @Scheduled(fixedRate = 600000)
    public void deleteExpiredToken(){
        List<Token> tokenList = tokenRepo.findAll();
        tokenList.stream().forEach(t->{
            Date date = new Date();
            if(!date.before(t.getCreatedDate()))
                tokenRepo.deleteById(t.getId());
        });
    }
}
