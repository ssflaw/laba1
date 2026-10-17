package com.example.demo.util;

public final class Patch {
    private Patch() {}

    /** Для PATCH: если пришло новое значение - берём его, иначе оставляем старое. */
    public static <T> T of(T newValue, T oldValue) {
        return newValue != null ? newValue : oldValue;
    }
}
