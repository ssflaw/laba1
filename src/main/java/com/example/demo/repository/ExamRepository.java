package com.example.demo.repository;

import com.example.demo.model.Exam;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Exam> findForUpdateById(Long id);
}
