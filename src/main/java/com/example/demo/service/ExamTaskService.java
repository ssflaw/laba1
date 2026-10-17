package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.ExamTask;
import com.example.demo.repository.ExamRepository;
import com.example.demo.repository.ExamTaskRepository;
import com.example.demo.util.Check;
import com.example.demo.util.Patch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamTaskService {

    private final ExamTaskRepository repository;
    private final ExamRepository examRepository;
    private final TaskSolutionService solutionService;

    public ExamTaskService(ExamTaskRepository repository, ExamRepository examRepository,
                           TaskSolutionService solutionService) {
        this.repository = repository;
        this.examRepository = examRepository;
        this.solutionService = solutionService;
    }

    public List<ExamTask> getAll() {
        return repository.findAll();
    }

    public ExamTask getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ExamTask " + id + " not found"));
    }

    public ExamTask create(ExamTask in) {
        Check.require(in.examId() != null && examRepository.findById(in.examId()).isPresent(),
                "exam with given examId does not exist");
        Check.require(Check.notBlank(in.condition()), "condition is required");
        Check.require(in.maxScore() != null && in.maxScore() > 0, "maxScore must be > 0");
        return repository.create(new ExamTask(null, in.examId(), in.condition(),
                in.referenceAnswer(), in.maxScore()));
    }

    public ExamTask update(Long id, ExamTask patch) {
        ExamTask old = getById(id);
        // examId менять нельзя - иначе сломаются связи с решениями
        ExamTask merged = new ExamTask(
                id,
                old.examId(),
                Patch.of(patch.condition(), old.condition()),
                Patch.of(patch.referenceAnswer(), old.referenceAnswer()),
                Patch.of(patch.maxScore(), old.maxScore()));
        Check.require(Check.notBlank(merged.condition()), "condition must not be blank");
        Check.require(merged.maxScore() > 0, "maxScore must be > 0");
        return repository.update(id, merged);
    }

    public void delete(Long id) {
        getById(id);
        solutionService.deleteByTaskId(id);
        repository.deleteById(id);
    }

    public void deleteByExamId(Long examId) {
        repository.findByExamId(examId).forEach(t -> delete(t.id()));
    }
}
