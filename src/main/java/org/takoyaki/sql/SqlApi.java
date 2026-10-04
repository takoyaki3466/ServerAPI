package org.takoyaki.sql;

import org.takoyaki.api.AbstractApi;
import org.takoyaki.http.ApiClient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class SqlApi extends AbstractApi {
    public static final String CHANGES_PATH = "/api/sql/changes";
    public static final String CHANGE_QUERY_PATH = "/api/sql/changes/query";
    public static final String QUERY_PATH = "/api/sql/query";
    public static final String ROWS_PATH = "/api/sql/rows";
    public static final String ROW_COUNT_PATH = "/api/sql/rows/count";
    public static final String SCHEMA_PATH = "/api/sql/schema";

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

    /** Executes a read-only SELECT statement. */
    public SqlQueryResult query(SqlQueryRequest request) {
        return post(QUERY_PATH, Objects.requireNonNull(request, "request cannot be null"), SqlQueryResult.class);
    }

    public SqlQueryResult query(String statement, List<SqlParameter> parameters) {
        return query(new SqlQueryRequest(statement, parameters, SqlQueryRequest.DEFAULT_LIMIT, 0));
    }

    public SqlQueryResult query(String statement, List<SqlParameter> parameters, int limit, int offset) {
        return query(new SqlQueryRequest(statement, parameters, limit, offset));
    }

    /** Gets rows using a structured query, without embedding identifiers in a URL. */
    public SqlQueryResult getRows(SqlTableQuery query) {
        return post(ROWS_PATH, Objects.requireNonNull(query, "query cannot be null"), SqlQueryResult.class);
    }

    public SqlQueryResult getAllRows(String tableName) {
        return getRows(SqlTableQuery.all(tableName));
    }

    public SqlQueryResult getRows(String tableName, int limit, int offset) {
        return getRows(new SqlTableQuery(tableName, List.of(), List.of(), List.of(), limit, offset));
    }

    public SqlQueryResult getRows(String tableName, List<SqlFilter> filters) {
        return getRows(SqlTableQuery.where(tableName, filters));
    }

    public SqlQueryResult getRows(String tableName, List<SqlFilter> filters, int limit, int offset) {
        return getRows(new SqlTableQuery(tableName, List.of(), filters, List.of(), limit, offset));
    }

    public Optional<SqlRow> getRow(String tableName, String keyColumn, SqlParameter keyValue) {
        SqlFilter keyFilter = SqlFilter.equal(keyColumn, keyValue);
        SqlTableQuery request = new SqlTableQuery(
                tableName,
                List.of(),
                List.of(keyFilter),
                List.of(),
                1,
                0
        );
        return getRows(request).firstRow();
    }

    public Optional<SqlRow> getRow(String tableName, List<SqlFilter> keyFilters) {
        SqlTableQuery request = new SqlTableQuery(tableName, List.of(), keyFilters, List.of(), 1, 0);
        return getRows(request).firstRow();
    }

    public SqlCountResult countRows(String tableName) {
        return countRows(tableName, List.of());
    }

    public SqlCountResult countRows(String tableName, List<SqlFilter> filters) {
        SqlCountRequest request = new SqlCountRequest(tableName, filters);
        return post(ROW_COUNT_PATH, request, SqlCountResult.class);
    }

    public SqlSchemaResult getSchema() {
        return get(SCHEMA_PATH, SqlSchemaResult.class);
    }

    public List<String> getTableNames() {
        return getSchema().tables().stream().map(SqlTableMetadata::name).toList();
    }

    public Optional<SqlTableMetadata> getTableMetadata(String tableName) {
        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("tableName cannot be empty");
        }
        return getSchema().tables().stream().filter(table -> table.name().equals(tableName)).findFirst();
    }

    public List<SqlColumnMetadata> getColumns(String tableName) {
        return getTableMetadata(tableName).map(SqlTableMetadata::columns).orElse(List.of());
    }

    public List<SqlColumnMetadata> getPrimaryKeyColumns(String tableName) {
        return getColumns(tableName).stream().filter(SqlColumnMetadata::primaryKey).toList();
    }

    /** Gets committed server-side changes after the specified revision for a diff view. */
    public SqlChangePage getChangesSince(long afterRevision) {
        return getChangesSince(afterRevision, SqlChangeQuery.DEFAULT_LIMIT);
    }

    public SqlChangePage getChangesSince(long afterRevision, int limit) {
        return post(CHANGE_QUERY_PATH, new SqlChangeQuery(afterRevision, limit), SqlChangePage.class);
    }
}
