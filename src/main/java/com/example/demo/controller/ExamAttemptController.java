package com.example.demo.controller;

import com.example.demo.model.ExamAttempt;
import com.example.demo.model.TaskSolution;
import com.example.demo.service.ExamAttemptService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('TEACHER')")
    public List<ExamAttempt> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or @access.ownsAttempt(#id)")
    public ExamAttempt getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT') and @access.isSelf(#body.studentId)")
    @ResponseStatus(HttpStatus.CREATED)
    public ExamAttempt start(@RequestBody ExamAttempt body) {
        return service.start(body.examId, body.studentId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/answers")
    @PreAuthorize("@access.ownsAttempt(#id)")
    public TaskSolution submitAnswer(@PathVariable Long id, @RequestBody TaskSolution body) {
        return service.submitAnswer(id, body.taskId, body.answer);
    }

    @PostMapping("/{id}/finish")
    @PreAuthorize("@access.ownsAttempt(#id)")
    public ExamAttempt finish(@PathVariable Long id) {
        return service.finish(id);
    }
}
