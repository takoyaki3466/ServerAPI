package org.takoyaki.sql;

import java.util.List;

public record SqlChangePage(long serverRevision, List<SqlCommittedChange> changes, boolean hasMore) {
    public SqlChangePage {
        if (serverRevision < 0) {
            throw new IllegalArgumentException("serverRevision cannot be negative");
        }
        changes = changes == null ? List.of() : List.copyOf(changes);
    }
}
