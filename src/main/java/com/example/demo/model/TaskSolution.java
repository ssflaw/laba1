package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "task_solutions")
public class TaskSolution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long attemptId;
    public Long taskId;
    public String answer;
    public Integer awardedScore;
}
