package com.example.demo.repository;

import com.example.demo.model.Exam;
import org.springframework.stereotype.Repository;

@Repository
public class ExamRepository extends InMemoryRepository<Exam> {
    @Override
    protected Exam withId(Exam e, Long id) {
        return new Exam(id, e.title(), e.passingScore(), e.timeLimitMinutes(), e.status());
    }
}
