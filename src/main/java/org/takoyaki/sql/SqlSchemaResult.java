package org.takoyaki.sql;

import java.util.List;

public record SqlSchemaResult(long serverRevision, List<SqlTableMetadata> tables) {
    public SqlSchemaResult {
        if (serverRevision < 0) {
            throw new IllegalArgumentException("serverRevision cannot be negative");
        }
        tables = tables == null ? List.of() : List.copyOf(tables);
    }
}
