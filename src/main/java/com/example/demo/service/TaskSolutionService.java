package com.example.demo.service;

import com.example.demo.model.TaskSolution;
import com.example.demo.repository.TaskSolutionRepository;
import com.example.demo.util.Check;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskSolutionService {

    private final TaskSolutionRepository repository;
    private final ExamAttemptService attemptService;

    public TaskSolutionService(TaskSolutionRepository repository, ExamAttemptService attemptService) {
        this.repository = repository;
        this.attemptService = attemptService;
    }

    public List<TaskSolution> getAll() {
        return repository.findAll();
    }

    public TaskSolution getById(Long id) {
        return Check.found(repository.findById(id), "TaskSolution " + id);
    }

    public TaskSolution create(TaskSolution in) {
        Check.require(in.attemptId != null, "attemptId is required");
        return attemptService.submitAnswer(in.attemptId, in.taskId, in.answer);
    }

    public TaskSolution update(Long id, TaskSolution patch) {
        TaskSolution old = getById(id);
        return attemptService.submitAnswer(old.attemptId, old.taskId, patch.answer);
    }

    @Transactional
    public void delete(Long id) {
        TaskSolution solution = getById(id);
        attemptService.requireInProgress(solution.attemptId);
        repository.delete(solution);
    }
}
