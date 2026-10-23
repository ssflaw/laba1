package com.example.demo.controller;

import com.example.demo.model.ExamTask;
import com.example.demo.service.ExamTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-tasks")
public class ExamTaskController {

    private final ExamTaskService service;

    public ExamTaskController(ExamTaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExamTask> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ExamTask getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExamTask create(@RequestBody ExamTask body) {
        return service.create(body);
    }

    @PatchMapping("/{id}")
    public ExamTask update(@PathVariable Long id, @RequestBody ExamTask body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
