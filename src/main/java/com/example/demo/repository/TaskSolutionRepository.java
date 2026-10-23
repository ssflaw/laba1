package com.example.demo.repository;

import com.example.demo.model.TaskSolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskSolutionRepository extends JpaRepository<TaskSolution, Long> {

    List<TaskSolution> findByAttemptId(Long attemptId);

    Optional<TaskSolution> findByAttemptIdAndTaskId(Long attemptId, Long taskId);
}
