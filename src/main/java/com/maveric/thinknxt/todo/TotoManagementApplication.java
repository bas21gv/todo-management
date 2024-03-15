package com.maveric.thinknxt.todo;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.maveric.thinknxt.todo.entity.Role;
import com.maveric.thinknxt.todo.entity.User;
import com.maveric.thinknxt.todo.repository.RoleRepository;
import com.maveric.thinknxt.todo.repository.UserRepository;

@SpringBootApplication
public class TotoManagementApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if(!userRepository.findByUsername("admin").isPresent()) {
                Role role = roleRepository.findByName("ROLE_ADMIN").orElseGet(
                        () -> roleRepository.save(new Role("ROLE_ADMIN"))
                );
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@gmail.com");
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Collections.singleton(role));
                userRepository.save(admin);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(TotoManagementApplication.class, args);
    }

}