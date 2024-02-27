package com.maveric.thinknxt.todo.controller;

import com.maveric.thinknxt.todo.dto.TodoDto;
import com.maveric.thinknxt.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@RequestBody TodoDto todoDto) {
        return new ResponseEntity<>(todoService.addTodo(todoDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodo(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodo(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<List<TodoDto>> getAllTodo() {
        return ResponseEntity.ok(todoService.getAllTodo());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodo(@PathVariable Long id, @RequestBody TodoDto todoDto) {
        return ResponseEntity.ok(todoService.updateTodo(id, todoDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteToto(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok("Deleted successfully!");
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TodoDto> completeTodo(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.completeTodo(id, Boolean.TRUE));
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/{id}/incomplete")
    public ResponseEntity<TodoDto> incompleteTodo(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.completeTodo(id, Boolean.FALSE));
    }
}
