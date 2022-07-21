package com.nsp.todo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Language extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30,nullable = false)
    private String language;
    @ManyToMany(mappedBy = "languages")
    private Set<User> users;
}
