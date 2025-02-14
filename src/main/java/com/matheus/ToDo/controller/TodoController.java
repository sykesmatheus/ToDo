package com.matheus.ToDo.controller;

import com.matheus.ToDo.entity.Todo;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.matheus.ToDo.service.TodoService;


import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    @GetMapping
    List<Todo> list(){
        return todoService.list();
    }
    @PostMapping
    List<Todo> create (@Valid @RequestBody  Todo todo){
        return todoService.create(todo);
    }
    @PutMapping
    List<Todo> update(@RequestBody Todo todo){
        return todoService.update(todo);
    }
    @DeleteMapping("{id}")
    List<Todo> delete(@PathVariable("id") Long id){
        return todoService.delete(id);
    }
}
