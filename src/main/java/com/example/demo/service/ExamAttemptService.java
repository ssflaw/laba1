package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.ExamAttempt;
import com.example.demo.repository.ExamAttemptRepository;
import com.example.demo.repository.ExamRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Check;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamAttemptService {

    private final ExamAttemptRepository repository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final TaskSolutionService solutionService;

    public ExamAttemptService(ExamAttemptRepository repository, ExamRepository examRepository,
                              UserRepository userRepository, TaskSolutionService solutionService) {
        this.repository = repository;
        this.examRepository = examRepository;
        this.userRepository = userRepository;
        this.solutionService = solutionService;
    }

    public List<ExamAttempt> getAll() {
        return repository.findAll();
    }

    public ExamAttempt getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ExamAttempt " + id + " not found"));
    }

    public ExamAttempt create(ExamAttempt in) {
        Check.require(in.examId() != null && examRepository.findById(in.examId()).isPresent(),
                "exam with given examId does not exist");
        Check.require(in.studentId() != null && userRepository.findById(in.studentId()).isPresent(),
                "user with given studentId does not exist");
        // время начала задаёт сервер, значения из запроса игнорируются
        return repository.create(new ExamAttempt(null, in.examId(), in.studentId(),
                LocalDateTime.now(), null, null, ExamAttempt.IN_PROGRESS));
    }

    /** PATCH: единственное допустимое изменение - завершение попытки (status = FINISHED). */
    public ExamAttempt update(Long id, ExamAttempt patch) {
        ExamAttempt old = getById(id);
        Check.require(patch.status() == null || ExamAttempt.FINISHED.equals(patch.status()),
                "only status FINISHED can be set");
        if (patch.status() == null || ExamAttempt.FINISHED.equals(old.status())) {
            return old;
        }
        int total = solutionService.calculateTotalScore(id);   // итоговый балл считает сервер
        return repository.update(id, new ExamAttempt(id, old.examId(), old.studentId(),
                old.startTime(), LocalDateTime.now(), total, ExamAttempt.FINISHED));
    }

    public void delete(Long id) {
        getById(id);
        solutionService.deleteByAttemptId(id);
        repository.deleteById(id);
    }

    public void deleteByExamId(Long examId) {
        repository.findByExamId(examId).forEach(a -> delete(a.id()));
    }

    public void deleteByStudentId(Long studentId) {
        repository.findByStudentId(studentId).forEach(a -> delete(a.id()));
    }
}
