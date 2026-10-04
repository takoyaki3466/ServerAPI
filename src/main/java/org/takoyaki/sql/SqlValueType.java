package org.takoyaki.sql;

/**
 * Portable parameter types that can be bound to a prepared SQL statement.
 */
public enum SqlValueType {
    NULL,
    STRING,
    INTEGER,
    DECIMAL,
    BOOLEAN,
    BYTES,
    JSON
}
