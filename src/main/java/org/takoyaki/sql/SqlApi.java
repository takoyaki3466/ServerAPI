package org.takoyaki.sql;

import org.takoyaki.api.AbstractApi;
import org.takoyaki.http.ApiClient;

import java.util.Objects;

public final class SqlApi extends AbstractApi {
    public static final String CHANGES_PATH = "/api/sql/changes";

    public SqlApi(ApiClient client) {
        super(client);
    }

    public SqlSyncResult pushChanges(SqlChangeBatch batch) {
        Objects.requireNonNull(batch, "batch cannot be null");
        if (batch.changes().isEmpty()) {
            return SqlSyncResult.noChanges(batch.baseRevision());
        }
        return post(CHANGES_PATH, batch, SqlSyncResult.class);
    }
}
