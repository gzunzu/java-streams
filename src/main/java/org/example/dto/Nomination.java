package org.example.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public record Nomination(String awards, String category, boolean won) {

    @Override
    public String toString() {
        return this.awards + " | " + this.category + (this.won ? " ★" : StringUtils.EMPTY);
    }

    static class NomitationComparator implements Comparator<Nomination> {

        @Override
        public int compare(Nomination o1, Nomination o2) {
            final int BEFORE = -1;
            final int EQUAL = 0;
            final int AFTER = 1;

            if (o1 == o2) return EQUAL;

            if (o1.won == o2.won) {
                if (o1.awards.equalsIgnoreCase(o2.awards)) {
                    return o1.category.compareTo(o2.category);
                } else {
                    return o1.awards.compareTo(o2.awards);
                }
            } else {
                return o1.won ? BEFORE : AFTER;
            }
        }
    }
}
