package com.example.demo.service;

import com.example.demo.model.Exam;
import com.example.demo.model.ExamTask;
import com.example.demo.repository.ExamRepository;
import com.example.demo.repository.ExamTaskRepository;
import com.example.demo.util.Check;
import com.example.demo.util.Patch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExamTaskService {

    private final ExamTaskRepository repository;
    private final ExamRepository examRepository;

    public ExamTaskService(ExamTaskRepository repository, ExamRepository examRepository) {
        this.repository = repository;
        this.examRepository = examRepository;
    }

    public List<ExamTask> getAll() {
        return repository.findAll();
    }

    public ExamTask getById(Long id) {
        return Check.found(repository.findById(id), "ExamTask " + id);
    }

    @Transactional
    public ExamTask create(ExamTask in) {
        in.id = null;
        requireDraftExam(in.examId);
        validate(in);
        return repository.save(in);
    }

    @Transactional
    public ExamTask update(Long id, ExamTask patch) {
        ExamTask task = getById(id);
        requireDraftExam(task.examId);
        task.condition = Patch.of(patch.condition, task.condition);
        task.referenceAnswer = Patch.of(patch.referenceAnswer, task.referenceAnswer);
        task.maxScore = Patch.of(patch.maxScore, task.maxScore);
        validate(task);
        return repository.save(task);
    }

    @Transactional
    public void delete(Long id) {
        ExamTask task = getById(id);
        requireDraftExam(task.examId);
        repository.delete(task);
    }

    private void requireDraftExam(Long examId) {
        Check.require(examId != null, "examId is required");
        Exam exam = Check.found(examRepository.findForUpdateById(examId), "Exam " + examId);
        Check.require(Exam.DRAFT.equals(exam.status), "tasks can be changed only in DRAFT exam");
    }

    private void validate(ExamTask t) {
        Check.require(Check.notBlank(t.condition), "condition is required");
        Check.require(t.referenceAnswer != null, "referenceAnswer is required");
        Check.require(t.maxScore != null && t.maxScore > 0, "maxScore must be > 0");
    }
}
