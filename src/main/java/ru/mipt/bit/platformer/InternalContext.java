package ru.mipt.bit.platformer;

import java.util.HashMap;
import java.util.Map;

/** Provides access to managers. */
public class InternalContext {
    /** */
    private final Map<Class<?>, Object> registry = new HashMap<>();

    /** */
    public <T> void register(Class<T> type, T instance) {
        registry.putIfAbsent(type, instance);
    }

    /** */
    public <T> T get(Class<T> type) {
        return (T)registry.get(type);
    }
}
