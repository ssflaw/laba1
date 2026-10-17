package com.example.demo.util;

import com.example.demo.exception.BadRequestException;

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
}
