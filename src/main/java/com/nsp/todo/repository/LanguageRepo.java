package com.nsp.todo.repository;

import com.nsp.todo.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepo extends JpaRepository<Language, Long> {
}
