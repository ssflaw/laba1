package com.example.demo.model;

import java.time.LocalDateTime;

public record ExamAttempt(
        Long id,
        Long examId,
        Long studentId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer totalScore,
        String status
) {
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String FINISHED = "FINISHED";
}
