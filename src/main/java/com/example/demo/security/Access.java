package com.example.demo.security;

import com.example.demo.repository.ExamAttemptRepository;
import com.example.demo.repository.TaskSolutionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** Контекстные проверки для @PreAuthorize: «это я» / «это моя попытка». */
@Component("access")
public class Access {

    private final UserRepository users;
    private final ExamAttemptRepository attempts;
    private final TaskSolutionRepository solutions;

    public Access(UserRepository users, ExamAttemptRepository attempts, TaskSolutionRepository solutions) {
        this.users = users;
        this.attempts = attempts;
        this.solutions = solutions;
    }

    public boolean isSelf(Long userId) {
        return userId != null && userId.equals(currentUserId());
    }

    public boolean ownsAttempt(Long attemptId) {
        return attemptId != null && attempts.findById(attemptId).map(a -> isSelf(a.studentId)).orElse(false);
    }

    public boolean ownsSolution(Long solutionId) {
        return solutionId != null && solutions.findById(solutionId).map(s -> ownsAttempt(s.attemptId)).orElse(false);
    }

    private Long currentUserId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return users.findByUsername(name).map(u -> u.id).orElse(null);
    }
}
