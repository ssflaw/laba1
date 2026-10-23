package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Check;
import com.example.demo.util.Patch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User getById(Long id) {
        return Check.found(repository.findById(id), "User " + id);
    }

    public User create(User in) {
        in.id = null;
        Check.require(Check.notBlank(in.username), "username is required");
        return repository.save(in);
    }

    public User update(Long id, User patch) {
        User user = getById(id);
        user.username = Patch.of(patch.username, user.username);
        user.email = Patch.of(patch.email, user.email);
        Check.require(Check.notBlank(user.username), "username must not be blank");
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.delete(getById(id));
    }
}
