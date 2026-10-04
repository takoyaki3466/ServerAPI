package org.takoyaki.sql;

public record SqlColumnMetadata(String name, String databaseType, boolean nullable, boolean primaryKey) {
    public SqlColumnMetadata {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        if (databaseType == null || databaseType.isBlank()) {
            throw new IllegalArgumentException("databaseType cannot be empty");
        }
    }
}
