package com.nsp.todo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Answer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 60)
    private String description;
    @Column(length = 90,nullable = false)
    private String path;
    @ManyToOne
    @JoinColumn(name = "task_id",referencedColumnName = "id")
    private Task task;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
}
