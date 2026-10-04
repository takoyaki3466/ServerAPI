package org.takoyaki.sql;

import java.time.Clock;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record SqlChange(String changeId, SqlOperation operation, String statement, List<SqlParameter> parameters,
                        long createdAtEpochMillis) {
    public SqlChange {
        if (changeId == null || changeId.isBlank()) {
            throw new IllegalArgumentException("changeId cannot be empty");
        }
        Objects.requireNonNull(operation, "operation cannot be null");
        if (statement == null || statement.isBlank()) {
            throw new IllegalArgumentException("statement cannot be empty");
        }
        parameters = parameters == null ? List.of() : List.copyOf(parameters);
        if (createdAtEpochMillis < 0) {
            throw new IllegalArgumentException("createdAtEpochMillis cannot be negative");
        }
    }

    public static SqlChange create(SqlOperation operation, String statement, List<SqlParameter> parameters) {
        return create(operation, statement, parameters, Clock.systemUTC());
    }

    static SqlChange create(SqlOperation operation, String statement, List<SqlParameter> parameters, Clock clock) {
        return new SqlChange(UUID.randomUUID().toString(), operation, statement, parameters, Objects.requireNonNull(clock).millis());
    }
}
