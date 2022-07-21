package com.nsp.todo.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 40,nullable = false)
    private String token;
    @Column(length = 30)
    private String email;
    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredDate;

    public Token(User user) {
        this.token = UUID.randomUUID().toString();
        this.user = user;
        this.createdDate = new Date();
        this.expiredDate = new Date(System.currentTimeMillis()+(10*60000));
        this.email = user.getEmail();
    }

    public Token(User user, String email) {
        this.token = UUID.randomUUID().toString();
        this.email = email;
        this.user = user;
        this.createdDate = new Date();
        this.expiredDate = new Date(System.currentTimeMillis()+(10*60000));
    }
}
