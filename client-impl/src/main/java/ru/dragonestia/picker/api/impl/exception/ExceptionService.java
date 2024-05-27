package ru.dragonestia.picker.api.impl.exception;

import org.reflections.Reflections;
import ru.dragonestia.picker.api.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

public class ExceptionService {

    private static Map<String, Class<?>> exceptionMap = null;

    public static void init() {
        if (exceptionMap != null) return;
        exceptionMap = new HashMap<>();

        var ref = new Reflections("ru.dragonestia.picker");
        for (var clazz: ref.getTypesAnnotatedWith(ApiException.class)) {
            exceptionMap.put(clazz.getSimpleName(), clazz);
        }
    }

    public static RuntimeException prepare(String ex, String message) {
        try {
            System.out.println(exceptionMap);
            return (RuntimeException) exceptionMap.getOrDefault(ex, UnknownException.class)
                    .getConstructor(String.class)
                    .newInstance(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
