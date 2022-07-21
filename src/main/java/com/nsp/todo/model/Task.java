package com.nsp.todo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
public class Task extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(length = 50,nullable = false)
//    private String title;
    @Column(length = 100)
    private String description;
    @Column(length = 90)
    private String path;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;
    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_tasks",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
    @OneToMany(mappedBy = "task",orphanRemoval = true)
    private Set<Answer> answers;
}
