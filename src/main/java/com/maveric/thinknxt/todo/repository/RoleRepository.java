package com.maveric.thinknxt.todo.repository;

import com.maveric.thinknxt.todo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
