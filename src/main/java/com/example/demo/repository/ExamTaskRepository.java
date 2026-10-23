package com.example.demo.repository;

import com.example.demo.model.ExamTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamTaskRepository extends JpaRepository<ExamTask, Long> {

    List<ExamTask> findByExamId(Long examId);
}
