package org.takoyaki.sql;

import java.util.List;
import java.util.Objects;

public record SqlFilter(String column, SqlComparisonOperator operator, List<SqlParameter> values) {
    public SqlFilter {
        if (column == null || column.isBlank()) {
            throw new IllegalArgumentException("column cannot be empty");
        }
        Objects.requireNonNull(operator, "operator cannot be null");
        values = values == null ? List.of() : List.copyOf(values);

        int valueCount = values.size();
        switch (operator) {
            case IS_NULL, IS_NOT_NULL -> {
                if (valueCount != 0) {
                    throw new IllegalArgumentException(operator + " does not accept values");
                }
            }
            case IN, NOT_IN -> {
                if (valueCount == 0) {
                    throw new IllegalArgumentException(operator + " requires at least one value");
                }
            }
            default -> {
                if (valueCount != 1) {
                    throw new IllegalArgumentException(operator + " requires exactly one value");
                }
            }
        }
    }

    public static SqlFilter equal(String column, SqlParameter value) {
        return new SqlFilter(column, SqlComparisonOperator.EQUAL, List.of(Objects.requireNonNull(value)));
    }

    public static SqlFilter notEqual(String column, SqlParameter value) {
        return new SqlFilter(column, SqlComparisonOperator.NOT_EQUAL, List.of(Objects.requireNonNull(value)));
    }

    public static SqlFilter in(String column, List<SqlParameter> values) {
        return new SqlFilter(column, SqlComparisonOperator.IN, values);
    }

    public static SqlFilter isNull(String column) {
        return new SqlFilter(column, SqlComparisonOperator.IS_NULL, List.of());
    }

    public static SqlFilter isNotNull(String column) {
        return new SqlFilter(column, SqlComparisonOperator.IS_NOT_NULL, List.of());
    }
}
