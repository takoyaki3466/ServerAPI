package org.takoyaki.http;

import com.fasterxml.jackson.core.type.TypeReference;
import org.takoyaki.config.ServerConfig;
import org.takoyaki.util.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public final class ApiClient {
    private static final String ACCEPT_HEADER = "Accept";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final int MIN_SUCCESS_STATUS = 200;
    private static final int MAX_SUCCESS_STATUS = 299;

    private final String baseUrl;
    private final HttpClient httpClient;
    private final ServerConfig config;

    public ApiClient(ServerConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config cannot be null");
        }

        this.config = config;
        this.baseUrl = config.baseUrl();
        this.httpClient = HttpClient.newBuilder().connectTimeout(config.connectTimeout()).build();
    }

    public <T> T get(String path, Class<T> responseType) {
        return request(HttpMethod.GET, path, null, responseType);
    }

    public <T> T get(String path, TypeReference<T> responseType) {
        return request(HttpMethod.GET, path, null, responseType);
    }

    public <T> T post(String path, Object body, Class<T> responseType) {
        return request(HttpMethod.POST, path, body, responseType);
    }

    public <T> T post(String path, Object body, TypeReference<T> responseType) {
        return request(HttpMethod.POST, path, body, responseType);
    }

    public <T> T put(String path, Object body, Class<T> responseType) {
        return request(HttpMethod.PUT, path, body, responseType);
    }

    public <T> T put(String path, Object body, TypeReference<T> responseType) {
        return request(HttpMethod.PUT, path, body, responseType);
    }

    public <T> T patch(String path, Object body, Class<T> responseType) {
        return request(HttpMethod.PATCH, path, body, responseType);
    }

    public <T> T patch(String path, Object body, TypeReference<T> responseType) {
        return request(HttpMethod.PATCH, path, body, responseType);
    }

    public void delete(String path) {
        request(HttpMethod.DELETE, path, null);
    }

    public String getJson(String path) {
        return sendJsonRequest(HttpMethod.GET, path, null).body();
    }

    public String sendJson(HttpMethod method, String path, String json) {
        if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
            throw new IllegalArgumentException("sendJson requires POST, PUT, or PATCH");
        }
        return sendJsonRequest(method, path, json).body();
    }

    private <T> T request(HttpMethod method, String path, Object body, Class<T> responseType) {
        HttpResponse<String> response = send(method, path, body);

        if (responseType == Void.class) {
            return null;
        }

        return JsonMapper.fromJson(response.body(), responseType);
    }

    private <T> T request(HttpMethod method, String path, Object body, TypeReference<T> responseType) {
        HttpResponse<String> response = send(method, path, body);
        return JsonMapper.fromJson(response.body(), responseType);
    }

    private void request(HttpMethod method, String path, Object body) {
        send(method, path, body);
    }

    private HttpResponse<String> send(HttpMethod method, String path, Object body) {
        String json = body == null ? null : JsonMapper.toJson(body);
        return sendJsonRequest(method, path, json);
    }

    private HttpResponse<String> sendJsonRequest(HttpMethod method, String path, String json) {
        String url = baseUrl + normalizePath(path);
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url)).timeout(config.requestTimeout()).header(ACCEPT_HEADER, JSON_CONTENT_TYPE);

        switch (method) {
            case GET -> builder.GET();
            case POST -> {
                builder.header(CONTENT_TYPE_HEADER, JSON_CONTENT_TYPE);
                builder.POST(jsonBody(json));
            }

            case PUT -> {
                builder.header(CONTENT_TYPE_HEADER, JSON_CONTENT_TYPE);
                builder.PUT(jsonBody(json));
            }

            case PATCH -> {
                builder.header(CONTENT_TYPE_HEADER, JSON_CONTENT_TYPE);
                builder.method(HttpMethod.PATCH.name(), jsonBody(json));
            }

            case DELETE -> builder.DELETE();

            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        try {
            HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            checkStatus(response);
            return response;

        } catch (IOException e) {
            throw new ApiException("Failed to connect to server", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("HTTP request interrupted", e);
        }
    }

    private HttpRequest.BodyPublisher jsonBody(String json) {
        if (json == null) {
            throw new IllegalArgumentException("A request body is required");
        }
        return HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8);
    }

    private void checkStatus(HttpResponse<String> response) {
        int statusCode = response.statusCode();

        if (statusCode >= MIN_SUCCESS_STATUS && statusCode <= MAX_SUCCESS_STATUS) {
            return;
        }

        throw new ApiException("API request failed: " + statusCode, statusCode, response.body());
    }

    private String normalizePath(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("path cannot be empty");
        }
        if (path.startsWith("/")) {
            return path;
        }

        return "/" + path;
    }
}
