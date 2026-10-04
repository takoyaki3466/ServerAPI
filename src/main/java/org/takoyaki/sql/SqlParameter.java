package org.takoyaki.sql;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Objects;

public record SqlParameter(SqlValueType type, String value) {
    public SqlParameter {
        Objects.requireNonNull(type, "type cannot be null");
        if (type != SqlValueType.NULL && value == null) {
            throw new IllegalArgumentException("value can be null only when type is NULL");
        }
        if (type == SqlValueType.NULL) {
            value = null;
        }
    }

    public static SqlParameter nullValue() {
        return new SqlParameter(SqlValueType.NULL, null);
    }

    public static SqlParameter string(String value) {
        return new SqlParameter(SqlValueType.STRING, value);
    }

    public static SqlParameter integer(long value) {
        return new SqlParameter(SqlValueType.INTEGER, Long.toString(value));
    }

    public static SqlParameter decimal(BigDecimal value) {
        return new SqlParameter(SqlValueType.DECIMAL, Objects.requireNonNull(value).toPlainString());
    }

    public static SqlParameter bool(boolean value) {
        return new SqlParameter(SqlValueType.BOOLEAN, Boolean.toString(value));
    }

    public static SqlParameter bytes(byte[] value) {
        return new SqlParameter(SqlValueType.BYTES, Base64.getEncoder().encodeToString(Objects.requireNonNull(value)));
    }

    public static SqlParameter json(String value) {
        return new SqlParameter(SqlValueType.JSON, value);
    }
}
