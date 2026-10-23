package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_tasks")
public class ExamTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long examId;
    public String condition;
    public String referenceAnswer;
    public Integer maxScore;
}
