package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Exam;
import com.example.demo.repository.ExamRepository;
import com.example.demo.util.Check;
import com.example.demo.util.Patch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    private final ExamRepository repository;
    private final ExamTaskService taskService;
    private final ExamAttemptService attemptService;

    public ExamService(ExamRepository repository, ExamTaskService taskService,
                       ExamAttemptService attemptService) {
        this.repository = repository;
        this.taskService = taskService;
        this.attemptService = attemptService;
    }

    public List<Exam> getAll() {
        return repository.findAll();
    }

    public Exam getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exam " + id + " not found"));
    }

    public Exam create(Exam in) {
        Check.require(Check.notBlank(in.title()), "title is required");
        Check.require(in.passingScore() != null && in.passingScore() >= 0, "passingScore must be >= 0");
        Check.require(in.timeLimitMinutes() != null && in.timeLimitMinutes() > 0,
                "timeLimitMinutes must be > 0");
        return repository.create(new Exam(null, in.title(), in.passingScore(),
                in.timeLimitMinutes(), Patch.of(in.status(), "DRAFT")));
    }

    public Exam update(Long id, Exam patch) {
        Exam old = getById(id);
        Exam merged = new Exam(
                id,
                Patch.of(patch.title(), old.title()),
                Patch.of(patch.passingScore(), old.passingScore()),
                Patch.of(patch.timeLimitMinutes(), old.timeLimitMinutes()),
                Patch.of(patch.status(), old.status()));
        Check.require(Check.notBlank(merged.title()), "title must not be blank");
        Check.require(merged.passingScore() >= 0, "passingScore must be >= 0");
        Check.require(merged.timeLimitMinutes() > 0, "timeLimitMinutes must be > 0");
        return repository.update(id, merged);
    }

    public void delete(Long id) {
        getById(id);
        taskService.deleteByExamId(id);      // каскадно: задания (и их решения)
        attemptService.deleteByExamId(id);   // каскадно: попытки (и их решения)
        repository.deleteById(id);
    }
}
