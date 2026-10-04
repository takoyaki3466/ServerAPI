package org.takoyaki.sql;

public enum SqlChangeStatus {
    APPLIED,
    DUPLICATE,
    CONFLICT,
    REJECTED
}
