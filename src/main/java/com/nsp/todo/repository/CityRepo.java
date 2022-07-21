package com.nsp.todo.repository;

import com.nsp.todo.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepo extends JpaRepository<City, Long> {
}
