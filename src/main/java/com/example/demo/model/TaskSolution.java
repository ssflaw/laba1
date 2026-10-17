package com.example.demo.model;

public record TaskSolution(
        Long id,
        Long attemptId,
        Long taskId,
        String answer,
        Integer awardedScore
) {}
