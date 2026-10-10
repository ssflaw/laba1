package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    // Первый GET-эндпоинт (возвращает текст)
    @GetMapping("/text")
    public String getText() {
        return "Привет! Это текстовый ответ.";
    }

    // Второй GET-эндпоинт (возвращает число)
    @GetMapping("/number")
    public int getNumber() {
        return 42;
    }
}