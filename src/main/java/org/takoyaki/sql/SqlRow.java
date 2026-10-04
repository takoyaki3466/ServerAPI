package org.takoyaki.sql;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public record SqlRow(Map<String, SqlValue> values) {
    public SqlRow {
        values = values == null ? Map.of() : Map.copyOf(new LinkedHashMap<>(values));
    }

    public Optional<SqlValue> get(String column) {
        return Optional.ofNullable(values.get(column));
    }

    public SqlValue require(String column) {
        SqlValue value = values.get(column);
        if (value == null) {
            throw new IllegalArgumentException("Unknown column: " + column);
        }
        return value;
    }
}
