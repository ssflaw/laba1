package com.example.demo.repository;

import com.example.demo.model.ExamTask;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExamTaskRepository extends InMemoryRepository<ExamTask> {
    @Override
    protected ExamTask withId(ExamTask t, Long id) {
        return new ExamTask(id, t.examId(), t.condition(), t.referenceAnswer(), t.maxScore());
    }

    public List<ExamTask> findByExamId(Long examId) {
        return findAll().stream().filter(t -> t.examId().equals(examId)).toList();
    }
}
