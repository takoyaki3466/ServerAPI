package org.takoyaki.sql;

import java.util.List;

public record SqlSyncResult(long serverRevision, List<SqlChangeResult> results) {
    public SqlSyncResult {
        if (serverRevision < 0) {
            throw new IllegalArgumentException("serverRevision cannot be negative");
        }
        results = results == null ? List.of() : List.copyOf(results);
    }

    public static SqlSyncResult noChanges(long revision) {
        return new SqlSyncResult(revision, List.of());
    }

    public boolean allAccepted() {
        return results.stream().allMatch(result -> result.status() == SqlChangeStatus.APPLIED || result.status() == SqlChangeStatus.DUPLICATE);
    }
}
