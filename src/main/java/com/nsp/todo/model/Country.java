package com.nsp.todo.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30,nullable = false)
    private String countryName;
    @OneToMany(mappedBy = "country",orphanRemoval = true)
    private Set<City> cities;
    @OneToMany(mappedBy = "country")
    private Set<Address> addresses;

    public static Country getUndefined(){
        return Country.builder()
                .countryName("undefined")
                .id(0L)
                .addresses(new HashSet<>())
                .cities(new HashSet<>())
                .build();
    }

}
