package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exams")
public class Exam {
    public static final String DRAFT = "DRAFT";
    public static final String PUBLISHED = "PUBLISHED";
    public static final String ARCHIVED = "ARCHIVED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String title;
    public Integer passingScore;
    public Integer timeLimitMinutes;
    public String status;
}
