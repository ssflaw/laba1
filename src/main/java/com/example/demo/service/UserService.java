package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Check;
import com.example.demo.util.Patch;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private static final Set<String> ROLES = Set.of(User.STUDENT, User.TEACHER);
    private static final Set<String> STATUSES = Set.of(User.ACTIVE, User.BLOCKED);

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User getById(Long id) {
        return Check.found(repository.findById(id), "User " + id);
    }

    public User getByUsername(String username) {
        return Check.found(repository.findByUsername(username), "User " + username);
    }

    /** Самостоятельная регистрация: всегда активный студент, роль из запроса игнорируется. */
    public User register(User in) {
        in.role = User.STUDENT;
        in.status = User.ACTIVE;
        return create(in);
    }

    /** Создание преподавателем: можно задать роль и статус. */
    public User create(User in) {
        in.id = null;
        in.role = Patch.of(in.role, User.STUDENT);
        in.status = Patch.of(in.status, User.ACTIVE);
        Check.require(Check.notBlank(in.username), "username is required");
        Check.require(!repository.existsByUsername(in.username), "username is already taken");
        Check.require(in.email == null || !repository.existsByEmail(in.email), "email is already taken");
        Check.require(ROLES.contains(in.role) && STATUSES.contains(in.status), "invalid role or status");
        Check.require(in.password != null && in.password.length() >= 8
                        && in.password.matches(".*[A-Za-z].*") && in.password.matches(".*\\d.*"),
                "password must be at least 8 characters long and contain letters and digits");
        in.password = encoder.encode(in.password);
        return repository.save(in);
    }

    public User update(Long id, User patch) {
        User user = getById(id);
        user.username = Patch.of(patch.username, user.username);
        user.email = Patch.of(patch.email, user.email);
        user.role = Patch.of(patch.role, user.role);
        user.status = Patch.of(patch.status, user.status);
        Check.require(Check.notBlank(user.username), "username must not be blank");
        Check.require(ROLES.contains(user.role) && STATUSES.contains(user.status), "invalid role or status");
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.delete(getById(id));
    }
}
