package org.takoyaki.api;

import com.fasterxml.jackson.core.type.TypeReference;
import org.takoyaki.http.ApiClient;

import java.util.Objects;

public abstract class AbstractApi {
    private final ApiClient client;

    protected AbstractApi(ApiClient client) {
        this.client = Objects.requireNonNull(client, "client cannot be null");
    }

    protected final <T> T get(String path, Class<T> responseType) {
        return client.get(path, responseType);
    }

    protected final <T> T get(String path, TypeReference<T> responseType) {
        return client.get(path, responseType);
    }

    protected final <T> T post(String path, Object body, Class<T> responseType) {
        return client.post(path, body, responseType);
    }

    protected final <T> T post(String path, Object body, TypeReference<T> responseType) {
        return client.post(path, body, responseType);
    }

    protected final <T> T put(String path, Object body, Class<T> responseType) {
        return client.put(path, body, responseType);
    }

    protected final <T> T patch(String path, Object body, Class<T> responseType) {
        return client.patch(path, body, responseType);
    }

    protected final void delete(String path) {
        client.delete(path);
    }
}
