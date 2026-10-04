package org.takoyaki.sql;

import java.util.List;

public record SqlCountRequest(String tableName, List<SqlFilter> filters) {
    public SqlCountRequest {
        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("tableName cannot be empty");
        }
        filters = filters == null ? List.of() : List.copyOf(filters);
    }
}
