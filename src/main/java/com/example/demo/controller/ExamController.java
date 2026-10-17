package com.example.demo.controller;

import com.example.demo.model.Exam;
import com.example.demo.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService service;

    public ExamController(ExamService service) {
        this.service = service;
    }

    @GetMapping
    public List<Exam> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Exam getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Exam create(@RequestBody Exam body) {
        return service.create(body);
    }

    @PatchMapping("/{id}")
    public Exam update(@PathVariable Long id, @RequestBody Exam body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}