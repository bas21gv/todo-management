package com.maveric.thinknxt.todo.controller;

import com.maveric.thinknxt.todo.dto.JwtAuthResponse;
import com.maveric.thinknxt.todo.dto.LoginDto;
import com.maveric.thinknxt.todo.dto.RegisterDto;
import com.maveric.thinknxt.todo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        return new ResponseEntity<>(authService.register(registerDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }
}
