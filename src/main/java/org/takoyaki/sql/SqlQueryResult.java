package org.takoyaki.sql;

import java.util.List;
import java.util.Optional;

public record SqlQueryResult(
        long serverRevision,
        List<String> columns,
        List<SqlRow> rows,
        long totalCount,
        boolean hasMore
) {
    public SqlQueryResult {
        if (serverRevision < 0) {
            throw new IllegalArgumentException("serverRevision cannot be negative");
        }
        columns = columns == null ? List.of() : List.copyOf(columns);
        rows = rows == null ? List.of() : List.copyOf(rows);
        if (totalCount < 0) {
            throw new IllegalArgumentException("totalCount cannot be negative");
        }
    }

    public Optional<SqlRow> firstRow() {
        return rows.stream().findFirst();
    }
}
