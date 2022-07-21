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
public class City extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30,nullable = false)
    private String cityName;
    @ManyToOne
    @JoinColumn(name = "country_id",referencedColumnName = "id")
    private Country country;
    @OneToMany(mappedBy = "city")
    private Set<Address> addresses;

    public static City getUndefined(){
        return City.builder()
                .cityName("undefined")
                .country(Country.getUndefined())
                .addresses(new HashSet<>())
                .build();
    }
}
