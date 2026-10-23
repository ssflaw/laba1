package com.example.demo.service;

import com.example.demo.model.Exam;
import com.example.demo.model.ExamAttempt;
import com.example.demo.model.ExamTask;
import com.example.demo.repository.ExamAttemptRepository;
import com.example.demo.repository.ExamRepository;
import com.example.demo.repository.ExamTaskRepository;
import com.example.demo.util.Check;
import com.example.demo.util.Patch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExamService {

    private final ExamRepository repository;
    private final ExamTaskRepository taskRepository;
    private final ExamAttemptRepository attemptRepository;

    public ExamService(ExamRepository repository, ExamTaskRepository taskRepository,
                       ExamAttemptRepository attemptRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
        this.attemptRepository = attemptRepository;
    }

    public List<Exam> getAll() {
        return repository.findAll();
    }

    public Exam getById(Long id) {
        return Check.found(repository.findById(id), "Exam " + id);
    }

    public Exam create(Exam in) {
        in.id = null;
        in.status = Exam.DRAFT;
        validate(in);
        return repository.save(in);
    }

    @Transactional
    public Exam update(Long id, Exam patch) {
        Exam exam = lock(id);
        Check.require(Exam.DRAFT.equals(exam.status), "only DRAFT exam can be edited");
        exam.title = Patch.of(patch.title, exam.title);
        exam.passingScore = Patch.of(patch.passingScore, exam.passingScore);
        exam.timeLimitMinutes = Patch.of(patch.timeLimitMinutes, exam.timeLimitMinutes);
        validate(exam);
        return repository.save(exam);
    }

    @Transactional
    public void delete(Long id) {
        Exam exam = lock(id);
        Check.require(!attemptRepository.existsByExamIdAndStatus(id, ExamAttempt.IN_PROGRESS),
                "exam has active attempts");
        repository.delete(exam);
    }

    @Transactional
    public Exam publish(Long id) {
        Exam exam = lock(id);
        Check.require(Exam.DRAFT.equals(exam.status), "only DRAFT exam can be published");
        List<ExamTask> tasks = taskRepository.findByExamId(id);
        Check.require(!tasks.isEmpty(), "exam has no tasks");
        int maxScore = tasks.stream().mapToInt(t -> t.maxScore).sum();
        Check.require(exam.passingScore <= maxScore, "passingScore is greater than max possible score " + maxScore);
        exam.status = Exam.PUBLISHED;
        return repository.save(exam);
    }

    @Transactional
    public Exam archive(Long id) {
        Exam exam = lock(id);
        Check.require(Exam.PUBLISHED.equals(exam.status), "only PUBLISHED exam can be archived");
        Check.require(!attemptRepository.existsByExamIdAndStatus(id, ExamAttempt.IN_PROGRESS),
                "exam has active attempts");
        exam.status = Exam.ARCHIVED;
        return repository.save(exam);
    }

    private Exam lock(Long id) {
        return Check.found(repository.findForUpdateById(id), "Exam " + id);
    }

    private void validate(Exam e) {
        Check.require(Check.notBlank(e.title), "title is required");
        Check.require(e.passingScore != null && e.passingScore >= 0, "passingScore must be >= 0");
        Check.require(e.timeLimitMinutes != null && e.timeLimitMinutes > 0, "timeLimitMinutes must be > 0");
    }
}
