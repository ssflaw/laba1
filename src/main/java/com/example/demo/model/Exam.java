package com.example.demo.model;

public record Exam(
        Long id,
        String title,
        Integer passingScore,
        Integer timeLimitMinutes,
        String status
) {}
