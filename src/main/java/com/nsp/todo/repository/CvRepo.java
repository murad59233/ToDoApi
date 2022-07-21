package com.nsp.todo.repository;

import com.nsp.todo.model.Cv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CvRepo extends JpaRepository<Cv,Long> {
}
