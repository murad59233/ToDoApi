package com.nsp.todo.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cv extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 70,nullable = false)
    private String path;
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
}
