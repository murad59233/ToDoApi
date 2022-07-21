package com.nsp.todo.repository;

import com.nsp.todo.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepo extends JpaRepository<Answer, Long> {
}
