package com.maveric.thinknxt.todo.service;

import com.maveric.thinknxt.todo.dto.JwtAuthResponse;
import com.maveric.thinknxt.todo.dto.LoginDto;
import com.maveric.thinknxt.todo.dto.RegisterDto;
import com.maveric.thinknxt.todo.entity.Role;
import com.maveric.thinknxt.todo.entity.User;
import com.maveric.thinknxt.todo.exception.TodoApiException;
import com.maveric.thinknxt.todo.repository.RoleRepository;
import com.maveric.thinknxt.todo.repository.UserRepository;
import com.maveric.thinknxt.todo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    public String register(RegisterDto registerDto) {

        if(userRepository.existsByUsername(registerDto.getUsername())) {
            throw new TodoApiException(HttpStatus.BAD_REQUEST, "Username is already exists!");
        }

        if(userRepository.existsByEmail(registerDto.getEmail())) {
            throw new TodoApiException(HttpStatus.BAD_REQUEST, "Email is already exists!");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_USER");
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return "User Registered Successfully!";
    }

    public JwtAuthResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        Optional<User> user = userRepository.findByUsername(loginDto.getUsername());
        String userRole = null;
        if (user.isPresent()) {
            Optional<Role> role = user.get().getRoles().stream().findFirst();
            if (role.isPresent()) {
                userRole = role.get().getName();
            }
        }
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        jwtAuthResponse.setRole(userRole);
        return jwtAuthResponse;
    }
}
