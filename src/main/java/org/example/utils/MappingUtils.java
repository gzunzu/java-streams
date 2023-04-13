package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MappingUtils {

    public static <T, E> Function<Map.Entry<T, E>, Pair<T, E>> mapEntryToPair() {
        return stringTEntry -> Pair.of(stringTEntry.getKey(), stringTEntry.getValue());
    }
}
