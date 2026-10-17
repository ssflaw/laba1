package com.example.demo.repository;

import com.example.demo.model.ExamAttempt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExamAttemptRepository extends InMemoryRepository<ExamAttempt> {
    @Override
    protected ExamAttempt withId(ExamAttempt a, Long id) {
        return new ExamAttempt(id, a.examId(), a.studentId(), a.startTime(), a.endTime(),
                a.totalScore(), a.status());
    }

    public List<ExamAttempt> findByExamId(Long examId) {
        return findAll().stream().filter(a -> a.examId().equals(examId)).toList();
    }

    public List<ExamAttempt> findByStudentId(Long studentId) {
        return findAll().stream().filter(a -> a.studentId().equals(studentId)).toList();
    }
}
