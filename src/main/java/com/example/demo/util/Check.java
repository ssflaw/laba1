package com.example.demo.util;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;

import java.util.Optional;

public final class Check {
    private Check() {}

    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new BadRequestException(message);
        }
    }

    public static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    public static <T> T found(Optional<T> value, String name) {
        return value.orElseThrow(() -> new NotFoundException(name + " not found"));
    }
}
