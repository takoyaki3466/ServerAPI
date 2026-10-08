package org.takoyaki;

import org.takoyaki.config.ServerClientInitializer;
import org.takoyaki.config.ServerConfig;
import org.takoyaki.gui.JsonTestGui;
import org.takoyaki.http.ApiClient;
import org.takoyaki.json.JsonApi;

public final class ServerClient {
    private final JsonApi jsonApi;

    public ServerClient(ServerConfig config) {
        ApiClient apiClient = new ApiClient(config);
        this.jsonApi = new JsonApi(apiClient);
    }

    public static ServerClientInitializer initialize() {
        return new ServerClientInitializer();
    }

    public JsonApi json() {
        return jsonApi;
    }

    public void openTestGui() {
        JsonTestGui.open(jsonApi);
    }
}
