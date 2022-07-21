package com.nsp.todo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 70)
    private String address;
    @Column(length = 20)
    private String zipCode;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id",referencedColumnName = "id")
    private Country country;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id",referencedColumnName = "id")
    private City city;
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

}
