package org.takoyaki.sql;

public record SqlCountResult(long serverRevision, long count) {
    public SqlCountResult {
        if (serverRevision < 0 || count < 0) {
            throw new IllegalArgumentException("serverRevision and count cannot be negative");
        }
    }
}
