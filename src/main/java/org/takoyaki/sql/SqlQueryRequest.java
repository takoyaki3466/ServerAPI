package org.takoyaki.sql;

import java.util.List;

public record SqlQueryRequest(String statement, List<SqlParameter> parameters, int limit, int offset) {
    public static final int DEFAULT_LIMIT = SqlTableQuery.DEFAULT_LIMIT;
    public static final int MAX_LIMIT = SqlTableQuery.MAX_LIMIT;

    public SqlQueryRequest {
        if (statement == null || statement.isBlank()) {
            throw new IllegalArgumentException("statement cannot be empty");
        }
        parameters = parameters == null ? List.of() : List.copyOf(parameters);
        if (limit < 1 || limit > MAX_LIMIT) {
            throw new IllegalArgumentException("limit must be between 1 and " + MAX_LIMIT);
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
    }
}
