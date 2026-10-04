package org.takoyaki.sql;

import java.util.List;

public record SqlTableQuery(
        String tableName,
        List<String> columns,
        List<SqlFilter> filters,
        List<SqlSort> sorts,
        int limit,
        int offset
) {
    public static final int DEFAULT_LIMIT = 100;
    public static final int MAX_LIMIT = 1_000;

    public SqlTableQuery {
        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("tableName cannot be empty");
        }
        columns = columns == null ? List.of() : List.copyOf(columns);
        if (columns.stream().anyMatch(column -> column == null || column.isBlank())) {
            throw new IllegalArgumentException("columns cannot contain empty names");
        }
        filters = filters == null ? List.of() : List.copyOf(filters);
        sorts = sorts == null ? List.of() : List.copyOf(sorts);
        if (limit < 1 || limit > MAX_LIMIT) {
            throw new IllegalArgumentException("limit must be between 1 and " + MAX_LIMIT);
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
    }

    public static SqlTableQuery all(String tableName) {
        return new SqlTableQuery(tableName, List.of(), List.of(), List.of(), DEFAULT_LIMIT, 0);
    }

    public static SqlTableQuery where(String tableName, List<SqlFilter> filters) {
        return new SqlTableQuery(tableName, List.of(), filters, List.of(), DEFAULT_LIMIT, 0);
    }
}
