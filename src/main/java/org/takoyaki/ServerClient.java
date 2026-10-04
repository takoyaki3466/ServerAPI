package org.takoyaki;

import org.takoyaki.http.ApiClient;
import org.takoyaki.config.ServerConfig;
import org.takoyaki.sql.SqlApi;
import org.takoyaki.status.StatusApi;

public final class ServerClient {
    private final StatusApi statusApi;
    private final SqlApi sqlApi;

    public ServerClient(String baseUrl) {
        this(ServerConfig.of(baseUrl));
    }

    public ServerClient(ServerConfig config) {
        ApiClient apiClient = new ApiClient(config);
        this.statusApi = new StatusApi(apiClient);
        this.sqlApi = new SqlApi(apiClient);
    }

    public StatusApi status() {
        return statusApi;
    }

    public SqlApi sql() {
        return sqlApi;
    }
}
