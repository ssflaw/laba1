package com.example.demo.repository;

import com.example.demo.model.TaskSolution;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskSolutionRepository extends InMemoryRepository<TaskSolution> {
    @Override
    protected TaskSolution withId(TaskSolution s, Long id) {
        return new TaskSolution(id, s.attemptId(), s.taskId(), s.answer(), s.awardedScore());
    }

    public List<TaskSolution> findByAttemptId(Long attemptId) {
        return findAll().stream().filter(s -> s.attemptId().equals(attemptId)).toList();
    }

    public List<TaskSolution> findByTaskId(Long taskId) {
        return findAll().stream().filter(s -> s.taskId().equals(taskId)).toList();
    }
}
