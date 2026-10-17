package com.example.demo.model;

public record ExamTask(
        Long id,
        Long examId,
        String condition,
        String referenceAnswer,
        Integer maxScore
) {}
