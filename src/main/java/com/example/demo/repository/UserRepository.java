package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends InMemoryRepository<User> {
    @Override
    protected User withId(User u, Long id) {
        return new User(id, u.username(), u.email());
    }
}
