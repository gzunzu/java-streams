package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextUtils {

    public static <T> String getAsPrettyString(final Collection<T> collection) {
        StringBuilder stringBuilder = new StringBuilder();
        if (CollectionUtils.isEmpty(collection)) {
            return StringUtils.EMPTY;
        }
        boolean firstElement = true;
        for (final T element : collection) {
            if (Objects.isNull(element)) {
                continue;
            }
            append(element, stringBuilder, firstElement);
            firstElement = false;
        }
        return stringBuilder.toString();
    }

    private static <T> void append(final T element, final StringBuilder stringBuilder, final boolean firstElement) {
        if (BooleanUtils.isFalse(firstElement)) {
            stringBuilder.append(" | ");
        }
        if (element instanceof Pair<?, ?> pair) {
            stringBuilder.append(StringUtils.capitalize(pair.toString("%s [%s]")));
        } else {
            stringBuilder.append(StringUtils.capitalize(element.toString()));
        }
    }

    public static <T> String getAsPrettyListString(final Collection<T> collection, final int indents) {
        StringBuilder stringBuilder = new StringBuilder();
        CollectionUtils.emptyIfNull(collection).stream()
                .filter(Objects::nonNull)
                .forEach(appendWithIndents(stringBuilder, indents));
        return stringBuilder.toString();
    }

    private static <T> Consumer<T> appendWithIndents(final StringBuilder stringBuilder, final int indents) {
        return element -> stringBuilder.append(System.lineSeparator())
                .append("\t".repeat(Math.max(0, indents)))
                .append(element);
    }
}
