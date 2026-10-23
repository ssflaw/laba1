package com.example.demo.util;

public final class Patch {
    private Patch() {}

    public static <T> T of(T newValue, T oldValue) {
        return newValue != null ? newValue : oldValue;
    }
}
