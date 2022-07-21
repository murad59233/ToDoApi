package com.nsp.todo.repository;

import com.nsp.todo.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepo extends JpaRepository<Technology, Long> {
}
