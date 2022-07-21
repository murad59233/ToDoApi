package com.nsp.todo.repository;

import com.nsp.todo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndEnabled(String username, boolean enabled);
    Optional<User> findByEmailAndEnabled(String email, boolean enabled);
    Optional<User> findByEmail(String email);
    @Transactional
    @Query("select u from User u where (u.email=:email or u.username=:username) and u.enabled=:enabled")
    Optional<User> findDisableUser(String username,String email,boolean enabled);

    @Transactional
    @Modifying
    @Query("update User u set u.enabled=:enabled where u.id=:userId")
    void enableUser(Long userId,boolean enabled);

    @Transactional
    @Modifying
    @Query("update User u set u.email=:email where u.id=:userId")
    void changeMail(Long userId,String email);
}
