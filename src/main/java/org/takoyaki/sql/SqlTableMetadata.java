package org.takoyaki.sql;

import java.util.List;

public record SqlTableMetadata(String name, List<SqlColumnMetadata> columns) {
    public SqlTableMetadata {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        columns = columns == null ? List.of() : List.copyOf(columns);
    }
}
