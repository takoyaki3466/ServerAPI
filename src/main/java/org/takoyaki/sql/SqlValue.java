package org.takoyaki.sql;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Objects;

public record SqlValue(SqlValueType type, String value) {
    public SqlValue {
        Objects.requireNonNull(type, "type cannot be null");
        if (type != SqlValueType.NULL && value == null) {
            throw new IllegalArgumentException("value can be null only when type is NULL");
        }
        if (type == SqlValueType.NULL) {
            value = null;
        }
    }

    public boolean isNull() {
        return type == SqlValueType.NULL;
    }

    public String asString() {
        return value;
    }

    public long asLong() {
        requireType(SqlValueType.INTEGER);
        return Long.parseLong(value);
    }

    public BigDecimal asDecimal() {
        requireType(SqlValueType.DECIMAL);
        return new BigDecimal(value);
    }

    public boolean asBoolean() {
        requireType(SqlValueType.BOOLEAN);
        return Boolean.parseBoolean(value);
    }

    public byte[] asBytes() {
        requireType(SqlValueType.BYTES);
        return Base64.getDecoder().decode(value);
    }

    private void requireType(SqlValueType expected) {
        if (type != expected) {
            throw new IllegalStateException("Expected " + expected + " but was " + type);
        }
    }
}
