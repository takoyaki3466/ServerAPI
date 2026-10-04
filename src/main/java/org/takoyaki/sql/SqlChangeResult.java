package org.takoyaki.sql;

public record SqlChangeResult(String changeId, SqlChangeStatus status, String message) {
    public SqlChangeResult {
        if (changeId == null || changeId.isBlank()) {
            throw new IllegalArgumentException("changeId cannot be empty");
        }
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
    }
}
