package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "exam_tasks")
public class ExamTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long examId;
    public String condition;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String referenceAnswer;
    public Integer maxScore;
}
