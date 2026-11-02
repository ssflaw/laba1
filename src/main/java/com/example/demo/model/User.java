package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    public static final String STUDENT = "STUDENT";
    public static final String TEACHER = "TEACHER";
    public static final String ACTIVE = "ACTIVE";
    public static final String BLOCKED = "BLOCKED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String username;
    public String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password_hash")
    public String password;
    public String role;
    public String status;
}
