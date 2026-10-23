package com.example.demo.repository;

import com.example.demo.model.ExamAttempt;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ExamAttempt> findForUpdateById(Long id);

    boolean existsByExamIdAndStatus(Long examId, String status);

    boolean existsByExamIdAndStudentIdAndStatus(Long examId, Long studentId, String status);
}
