package org.takoyaki.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Arrays;

public record ServerConfig(String baseUrl, Duration connectTimeout, Duration requestTimeout) {
    public static final String EMPTY_PATH = "";
    public static final int MIN_PORT = 1;
    public static final int MAX_PORT = 65_535;

    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(10);

    public ServerConfig {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("baseUrl cannot be empty");
        }

        if (connectTimeout == null) {
            connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        }

        if (requestTimeout == null) {
            requestTimeout = DEFAULT_REQUEST_TIMEOUT;
        }

        if (connectTimeout.isZero() || connectTimeout.isNegative()) {
            throw new IllegalArgumentException("connectTimeout must be positive");
        }

        if (requestTimeout.isZero() || requestTimeout.isNegative()) {
            throw new IllegalArgumentException("requestTimeout must be positive");
        }

        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        URI uri = URI.create(baseUrl);
        if (uri.getScheme() == null || uri.getHost() == null) {
            throw new IllegalArgumentException("baseUrl must be an absolute HTTP(S) URL");
        }

        String scheme = uri.getScheme();
        boolean supportedScheme = Arrays.stream(HttpScheme.values())
                .anyMatch(value -> value.value().equalsIgnoreCase(scheme));
        if (!supportedScheme) {
            throw new IllegalArgumentException("baseUrl must use HTTP or HTTPS");
        }
    }

    public static ServerConfig create(
            HttpScheme scheme,
            String host,
            int port,
            String basePath,
            Duration connectTimeout,
            Duration requestTimeout
    ) {
        if (scheme == null) {
            throw new IllegalArgumentException("scheme cannot be null");
        }
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("host cannot be empty");
        }
        if (port < MIN_PORT || port > MAX_PORT) {
            throw new IllegalArgumentException("port must be between " + MIN_PORT + " and " + MAX_PORT);
        }

        String normalizedPath = normalizeBasePath(basePath);
        try {
            URI uri = new URI(scheme.value(), null, host, port, normalizedPath, null, null);
            return new ServerConfig(uri.toString(), connectTimeout, requestTimeout);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid server address", e);
        }
    }

    private static String normalizeBasePath(String basePath) {
        if (basePath == null || basePath.isBlank() || "/".equals(basePath)) {
            return EMPTY_PATH;
        }

        String normalized = basePath.startsWith("/") ? basePath : "/" + basePath;
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
