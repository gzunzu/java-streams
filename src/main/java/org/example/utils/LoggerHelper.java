package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;

import java.util.function.Consumer;

import static java.util.Arrays.stream;

@NoArgsConstructor(access = AccessLevel.NONE)
public class LoggerHelper {

    public static void shutDownLogs(Class<?>... classes) {
        stream(classes).forEach(shutDownClassLog());
    }

    private static Consumer<Class<?>> shutDownClassLog() {
        return clazz -> LogManager.getFactory().shutdown(clazz.getCanonicalName(),
                clazz.getClassLoader(),
                false,
                true);
    }
}
