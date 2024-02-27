package com.maveric.thinknxt.todo.service;

import com.maveric.thinknxt.todo.dto.TodoDto;
import com.maveric.thinknxt.todo.entity.Todo;
import com.maveric.thinknxt.todo.exception.ResourceNotFoundException;
import com.maveric.thinknxt.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;
    public TodoDto addTodo(TodoDto todoDto) {
        Todo todo = modelMapper.map(todoDto, Todo.class);
        Todo savedTodo = todoRepository.save(todo);
        return modelMapper.map(savedTodo, TodoDto.class);
    }

    public TodoDto getTodo(long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Todo not exists with given id:"+id));
        return modelMapper.map(todo, TodoDto.class);
    }

    public List<TodoDto> getAllTodo() {
        List<Todo> todos = todoRepository.findAll();
        return todos.stream().map(e-> modelMapper.map(e, TodoDto.class)).collect(Collectors.toList());
    }

    public TodoDto updateTodo(long id, TodoDto todoDto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Todo not exists with given id:"+id));
        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        todo.setCompleted(todoDto.getCompleted());

        Todo updatedTodo = todoRepository.save(todo);
        return modelMapper.map(updatedTodo, TodoDto.class);
    }

    public void deleteTodo(long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Todo not exists with given id:"+id));
        todoRepository.delete(todo);
    }

    public TodoDto completeTodo(long id, boolean complete) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Todo not exists with given id:"+id));
        if (complete) {
            todo.setCompleted(Boolean.TRUE);
        } else {
            todo.setCompleted(Boolean.FALSE);
        }
        Todo updatedTodo = todoRepository.save(todo);
        return modelMapper.map(updatedTodo, TodoDto.class);
    }
}
