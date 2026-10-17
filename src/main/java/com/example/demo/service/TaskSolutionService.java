package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.ExamAttempt;
import com.example.demo.model.ExamTask;
import com.example.demo.model.TaskSolution;
import com.example.demo.repository.ExamAttemptRepository;
import com.example.demo.repository.ExamTaskRepository;
import com.example.demo.repository.TaskSolutionRepository;
import com.example.demo.util.Check;
import com.example.demo.util.Patch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskSolutionService {

    private final TaskSolutionRepository repository;
    private final ExamAttemptRepository attemptRepository;
    private final ExamTaskRepository taskRepository;

    public TaskSolutionService(TaskSolutionRepository repository,
                               ExamAttemptRepository attemptRepository,
                               ExamTaskRepository taskRepository) {
        this.repository = repository;
        this.attemptRepository = attemptRepository;
        this.taskRepository = taskRepository;
    }

    public List<TaskSolution> getAll() {
        return repository.findAll();
    }

    public TaskSolution getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("TaskSolution " + id + " not found"));
    }

    public TaskSolution create(TaskSolution in) {
        Check.require(in.attemptId() != null && in.taskId() != null,
                "attemptId and taskId are required");
        ExamAttempt attempt = attemptRepository.findById(in.attemptId())
                .orElseThrow(() -> new BadRequestException("attempt does not exist"));
        ExamTask task = taskRepository.findById(in.taskId())
                .orElseThrow(() -> new BadRequestException("task does not exist"));

        Check.require(ExamAttempt.IN_PROGRESS.equals(attempt.status()), "attempt is already finished");
        Check.require(task.examId().equals(attempt.examId()),
                "task does not belong to the exam of this attempt");
        Check.require(repository.findByAttemptId(attempt.id()).stream()
                        .noneMatch(s -> s.taskId().equals(task.id())),
                "solution for this task already exists in this attempt");

        // баллы начисляет сервер, значение из запроса игнорируется
        return repository.create(new TaskSolution(null, attempt.id(), task.id(),
                in.answer(), grade(task, in.answer())));
    }

    /** PATCH: можно менять только ответ, баллы пересчитываются. */
    public TaskSolution update(Long id, TaskSolution patch) {
        TaskSolution old = getById(id);
        ExamAttempt attempt = attemptRepository.findById(old.attemptId())
                .orElseThrow(() -> new NotFoundException("attempt not found"));
        ExamTask task = taskRepository.findById(old.taskId())
                .orElseThrow(() -> new NotFoundException("task not found"));
        Check.require(ExamAttempt.IN_PROGRESS.equals(attempt.status()), "attempt is already finished");

        String answer = Patch.of(patch.answer(), old.answer());
        return repository.update(id, new TaskSolution(id, old.attemptId(), old.taskId(),
                answer, grade(task, answer)));
    }

    public void delete(Long id) {
        getById(id);
        repository.deleteById(id);
    }

    public void deleteByAttemptId(Long attemptId) {
        repository.findByAttemptId(attemptId).forEach(s -> repository.deleteById(s.id()));
    }

    public void deleteByTaskId(Long taskId) {
        repository.findByTaskId(taskId).forEach(s -> repository.deleteById(s.id()));
    }

    public int calculateTotalScore(Long attemptId) {
        return repository.findByAttemptId(attemptId).stream()
                .mapToInt(TaskSolution::awardedScore)
                .sum();
    }

    /** Полный балл, если ответ совпал с эталонным (без учёта регистра и пробелов по краям), иначе 0. */
    private int grade(ExamTask task, String answer) {
        if (answer == null || task.referenceAnswer() == null) {
            return 0;
        }
        return answer.trim().equalsIgnoreCase(task.referenceAnswer().trim()) ? task.maxScore() : 0;
    }
}
