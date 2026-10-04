package org.takoyaki.sql;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public record SqlChangeBatch(String sourceId, long baseRevision, boolean atomic, List<SqlChange> changes) {
    public SqlChangeBatch {
        if (sourceId == null || sourceId.isBlank()) {
            throw new IllegalArgumentException("sourceId cannot be empty");
        }
        if (baseRevision < 0) {
            throw new IllegalArgumentException("baseRevision cannot be negative");
        }
        changes = changes == null ? List.of() : List.copyOf(changes);

        Set<String> changeIds = new HashSet<>();
        for (SqlChange change : changes) {
            if (!changeIds.add(change.changeId())) {
                throw new IllegalArgumentException("changeId must be unique within a batch: " + change.changeId());
            }
        }
    }
}
