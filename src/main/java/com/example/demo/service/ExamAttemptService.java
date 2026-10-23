package com.example.demo.service;

import com.example.demo.model.Exam;
import com.example.demo.model.ExamAttempt;
import com.example.demo.model.ExamTask;
import com.example.demo.model.TaskSolution;
import com.example.demo.repository.ExamAttemptRepository;
import com.example.demo.repository.ExamRepository;
import com.example.demo.repository.ExamTaskRepository;
import com.example.demo.repository.TaskSolutionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Check;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamAttemptService {

    private final ExamAttemptRepository repository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final ExamTaskRepository taskRepository;
    private final TaskSolutionRepository solutionRepository;

    public ExamAttemptService(ExamAttemptRepository repository, ExamRepository examRepository,
                              UserRepository userRepository, ExamTaskRepository taskRepository,
                              TaskSolutionRepository solutionRepository) {
        this.repository = repository;
        this.examRepository = examRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.solutionRepository = solutionRepository;
    }

    public List<ExamAttempt> getAll() {
        return repository.findAll();
    }

    public ExamAttempt getById(Long id) {
        return Check.found(repository.findById(id), "ExamAttempt " + id);
    }

    public void delete(Long id) {
        repository.delete(getById(id));
    }

    @Transactional
    public ExamAttempt start(Long examId, Long studentId) {
        Check.require(examId != null && studentId != null, "examId and studentId are required");
        Exam exam = Check.found(examRepository.findForUpdateById(examId), "Exam " + examId);
        Check.found(userRepository.findById(studentId), "User " + studentId);
        Check.require(Exam.PUBLISHED.equals(exam.status), "exam is not published");
        Check.require(!repository.existsByExamIdAndStudentIdAndStatus(examId, studentId, ExamAttempt.IN_PROGRESS),
                "student already has an active attempt for this exam");

        ExamAttempt attempt = new ExamAttempt();
        attempt.examId = examId;
        attempt.studentId = studentId;
        attempt.startTime = LocalDateTime.now();
        attempt.status = ExamAttempt.IN_PROGRESS;
        return repository.save(attempt);
    }

    @Transactional
    public TaskSolution submitAnswer(Long attemptId, Long taskId, String answer) {
        Check.require(taskId != null && answer != null, "taskId and answer are required");
        ExamAttempt attempt = lock(attemptId);
        ExamTask task = Check.found(taskRepository.findById(taskId), "ExamTask " + taskId);
        Exam exam = Check.found(examRepository.findById(attempt.examId), "Exam " + attempt.examId);

        Check.require(ExamAttempt.IN_PROGRESS.equals(attempt.status), "attempt is already finished");
        Check.require(task.examId.equals(attempt.examId), "task does not belong to the exam of this attempt");
        Check.require(!LocalDateTime.now().isAfter(attempt.startTime.plusMinutes(exam.timeLimitMinutes)),
                "time limit is over, finish the attempt");

        TaskSolution solution = solutionRepository.findByAttemptIdAndTaskId(attemptId, taskId)
                .orElseGet(TaskSolution::new);
        solution.attemptId = attemptId;
        solution.taskId = taskId;
        solution.answer = answer;
        solution.awardedScore = answer.trim().equalsIgnoreCase(task.referenceAnswer.trim()) ? task.maxScore : 0;
        return solutionRepository.save(solution);
    }

    @Transactional
    public ExamAttempt finish(Long id) {
        ExamAttempt attempt = lock(id);
        Check.require(ExamAttempt.IN_PROGRESS.equals(attempt.status), "attempt is already finished");
        Exam exam = Check.found(examRepository.findById(attempt.examId), "Exam " + attempt.examId);

        int total = solutionRepository.findByAttemptId(id).stream().mapToInt(s -> s.awardedScore).sum();
        attempt.totalScore = total;
        attempt.endTime = LocalDateTime.now();
        attempt.status = total >= exam.passingScore ? ExamAttempt.PASSED : ExamAttempt.FAILED;
        return repository.save(attempt);
    }

    @Transactional
    public void requireInProgress(Long attemptId) {
        Check.require(ExamAttempt.IN_PROGRESS.equals(lock(attemptId).status), "attempt is already finished");
    }

    private ExamAttempt lock(Long id) {
        return Check.found(repository.findForUpdateById(id), "ExamAttempt " + id);
    }
}
