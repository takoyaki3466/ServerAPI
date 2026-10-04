package org.takoyaki.sql;

import java.util.Objects;

/** A change accepted by the server, paired with its monotonically increasing revision. */
public record SqlCommittedChange(long revision, SqlChange change) {
    public SqlCommittedChange {
        if (revision < 1) {
            throw new IllegalArgumentException("revision must be positive");
        }
        Objects.requireNonNull(change, "change cannot be null");
    }
}
