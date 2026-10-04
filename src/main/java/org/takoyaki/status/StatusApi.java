package org.takoyaki.status;

import org.takoyaki.api.AbstractApi;
import org.takoyaki.http.ApiClient;

public final class StatusApi extends AbstractApi {
    private static final String STATUS_PATH = "/api/status";

    public StatusApi(ApiClient client) {
        super(client);
    }

    public ServerStatus get() {
        return get(STATUS_PATH, ServerStatus.class);
    }
}
