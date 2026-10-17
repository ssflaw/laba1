package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Check;
import com.example.demo.util.Patch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final ExamAttemptService attemptService;

    public UserService(UserRepository repository, ExamAttemptService attemptService) {
        this.repository = repository;
        this.attemptService = attemptService;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User " + id + " not found"));
    }

    public User create(User in) {
        Check.require(Check.notBlank(in.username()), "username is required");
        return repository.create(new User(null, in.username(), in.email()));
    }

    public User update(Long id, User patch) {
        User old = getById(id);
        User merged = new User(id,
                Patch.of(patch.username(), old.username()),
                Patch.of(patch.email(), old.email()));
        Check.require(Check.notBlank(merged.username()), "username must not be blank");
        return repository.update(id, merged);
    }

    public void delete(Long id) {
        getById(id);
        attemptService.deleteByStudentId(id);   // каскадно: попытки пользователя
        repository.deleteById(id);
    }
}
