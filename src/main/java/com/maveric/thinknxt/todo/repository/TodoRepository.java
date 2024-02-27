package com.maveric.thinknxt.todo.repository;

import com.maveric.thinknxt.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
