package org.takoyaki.config;

import org.takoyaki.ServerClient;

import java.time.Duration;

public final class ServerClientInitializer {
    private HttpScheme scheme;
    private String host;
    private Integer port;
    private String basePath = ServerConfig.EMPTY_PATH;
    private Duration connectTimeout;
    private Duration requestTimeout;

    public ServerClientInitializer scheme(HttpScheme scheme) {
        this.scheme = scheme;
        return this;
    }

    public ServerClientInitializer host(String host) {
        this.host = host;
        return this;
    }

    public ServerClientInitializer port(int port) {
        this.port = port;
        return this;
    }

    public ServerClientInitializer basePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public ServerClientInitializer connectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public ServerClientInitializer requestTimeout(Duration requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public ServerClient build() {
        if (scheme == null) {
            throw new IllegalStateException("scheme must be configured");
        }
        if (host == null || host.isBlank()) {
            throw new IllegalStateException("host must be configured");
        }
        if (port == null) {
            throw new IllegalStateException("port must be configured");
        }

        ServerConfig config = ServerConfig.create(
                scheme,
                host,
                port,
                basePath,
                connectTimeout,
                requestTimeout
        );
        return new ServerClient(config);
    }
}
