package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_attempts")
public class ExamAttempt {
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String PASSED = "PASSED";
    public static final String FAILED = "FAILED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long examId;
    public Long studentId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public Integer totalScore;
    public String status;
}
