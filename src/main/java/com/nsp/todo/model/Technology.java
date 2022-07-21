package com.nsp.todo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Technology extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30,nullable = false)
    private String technology;
    @ManyToMany(mappedBy = "technology")
    private Set<User> users;
}
