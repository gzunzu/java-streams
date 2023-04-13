package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextUtils {

    public static <T> String getAsPrettyString(final Collection<T> collection) {
        StringBuilder result = new StringBuilder();
        if (CollectionUtils.isEmpty(collection)) {
            return StringUtils.EMPTY;
        }
        boolean firstElement = true;
        for (final T element : collection) {
            if (Objects.isNull(element)) {
                continue;
            }
            addElement(result, element, firstElement);
            firstElement = false;
        }
        return result.toString();
    }

    private static <T> void addElement(final StringBuilder stringBuilder, final T element, final boolean firstElement) {
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
        StringBuilder result = new StringBuilder();
        if (CollectionUtils.isEmpty(collection)) {
            return StringUtils.EMPTY;
        }
        for (final T element : collection) {
            if (Objects.isNull(element)) {
                continue;
            }
            result.append(System.lineSeparator());
            result.append("\t".repeat(Math.max(0, indents)));
            result.append(element);
        }
        return result.toString();
    }
}
