package com.matheus.ToDo.service;

import com.matheus.ToDo.entity.Todo;
import com.matheus.ToDo.repository.TodoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TodoService {
    private TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> create(Todo todo) {
        todoRepository.save(todo);
        return list();
    }

    public List<Todo> update(Long id, Todo newTodo) {
        Todo oldTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
        if (newTodo.getNome().isBlank() || newTodo.getDescricao().isBlank() || newTodo.getPrioridade()<=0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível adicionar em branco/priorida < 1");
        }
        oldTodo.setNome(newTodo.getNome());
        oldTodo.setDescricao(newTodo.getDescricao());
        oldTodo.setPrioridade(newTodo.getPrioridade());
        oldTodo.setRealizado(newTodo.isRealizado());
        todoRepository.save(oldTodo);
        return list();
    }



    public List<Todo> delete(Long id){
        todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
        todoRepository.deleteById(id);
        return list();
    }
    public List<Todo> list(){
        Sort sort = Sort.by("prioridade").descending().and(
                Sort.by("nome").ascending());
        return todoRepository.findAll(sort);
    }
}
