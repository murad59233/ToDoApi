package com.nsp.todo.model;

import com.nsp.todo.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30,nullable = false)
    private String name;
    @Column(length = 30,nullable = false)
    private String surname;
    @Column(length = 30,nullable = false,unique = true)
    private String username;
    @Column(length = 30,nullable = false,unique = true)
    private String email;
    @Column(length = 30,nullable = false)
    private String phone;
    @Column(length = 90,nullable = false)
    private String password;
    @Column(length = 50)
    private String github;
    @Builder.Default
    private Role role = Role.USER;
    @Builder.Default
    private Boolean enabled = false;
    @OneToOne(mappedBy = "user",orphanRemoval = true,fetch = FetchType.EAGER)
    private Address address;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinTable(
            name = "user_languages",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<Language> languages;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinTable(
            name = "user_technlogy",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private Set<Technology> technology;
    @ManyToMany(mappedBy = "users",fetch = FetchType.EAGER)
    private Set<Notification> notifications;
    @ManyToMany(mappedBy = "users",fetch = FetchType.EAGER)
    private Set<Task> tasks;
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private Set<Answer> answers;
    @OneToOne(mappedBy = "user",orphanRemoval = true,fetch = FetchType.EAGER)
    private Cv cv;
    @OneToOne(mappedBy = "user",orphanRemoval = true,fetch = FetchType.EAGER)
    private Token token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority(role.name())
        );
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
