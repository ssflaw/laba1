package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/text")
    public String getText() {
        return "Привет! Это текстовый ответ.";
    }

    @GetMapping("/number")
    public int getNumber() {
        return 42;
    }
}
