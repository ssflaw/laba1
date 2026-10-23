package com.example.demo.controller;

import com.example.demo.model.TaskSolution;
import com.example.demo.service.TaskSolutionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-solutions")
public class TaskSolutionController {

    private final TaskSolutionService service;

    public TaskSolutionController(TaskSolutionService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskSolution> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public TaskSolution getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskSolution create(@RequestBody TaskSolution body) {
        return service.create(body);
    }

    @PatchMapping("/{id}")
    public TaskSolution update(@PathVariable Long id, @RequestBody TaskSolution body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
