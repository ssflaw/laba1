package com.example.demo.controller;

import com.example.demo.model.ExamAttempt;
import com.example.demo.model.TaskSolution;
import com.example.demo.service.ExamAttemptService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-attempts")
public class ExamAttemptController {

    private final ExamAttemptService service;

    public ExamAttemptController(ExamAttemptService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExamAttempt> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ExamAttempt getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExamAttempt start(@RequestBody ExamAttempt body) {
        return service.start(body.examId, body.studentId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/answers")
    public TaskSolution submitAnswer(@PathVariable Long id, @RequestBody TaskSolution body) {
        return service.submitAnswer(id, body.taskId, body.answer);
    }

    @PostMapping("/{id}/finish")
    public ExamAttempt finish(@PathVariable Long id) {
        return service.finish(id);
    }
}
