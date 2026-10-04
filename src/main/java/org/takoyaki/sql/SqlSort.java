package org.takoyaki.sql;

import java.util.Objects;

public record SqlSort(String column, SqlSortDirection direction) {
    public SqlSort {
        if (column == null || column.isBlank()) {
            throw new IllegalArgumentException("column cannot be empty");
        }
        Objects.requireNonNull(direction, "direction cannot be null");
    }

    public static SqlSort ascending(String column) {
        return new SqlSort(column, SqlSortDirection.ASCENDING);
    }

    public static SqlSort descending(String column) {
        return new SqlSort(column, SqlSortDirection.DESCENDING);
    }
}
