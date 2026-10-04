package org.takoyaki.sql;

public record SqlChangeQuery(long afterRevision, int limit) {
    public static final int DEFAULT_LIMIT = 100;
    public static final int MAX_LIMIT = 1_000;

    public SqlChangeQuery {
        if (afterRevision < 0) {
            throw new IllegalArgumentException("afterRevision cannot be negative");
        }
        if (limit < 1 || limit > MAX_LIMIT) {
            throw new IllegalArgumentException("limit must be between 1 and " + MAX_LIMIT);
        }
    }
}
