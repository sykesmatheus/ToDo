package com.matheus.ToDo.repository;

import com.matheus.ToDo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
