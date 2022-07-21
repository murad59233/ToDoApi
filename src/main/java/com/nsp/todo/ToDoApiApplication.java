package com.nsp.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class ToDoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToDoApiApplication.class, args);
    }

}
