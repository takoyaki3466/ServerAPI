package org.takoyaki.json;

import com.fasterxml.jackson.core.type.TypeReference;
import org.takoyaki.api.AbstractApi;
import org.takoyaki.http.ApiClient;
import org.takoyaki.util.JsonMapper;

import java.util.Objects;

public final class JsonApi extends AbstractApi {
    public static final JsonMethod DEFAULT_SETTER_METHOD = JsonMethod.POST;

    public JsonApi(ApiClient client) {
        super(client);
    }

    public String getter(String path) {
        return getJson(path);
    }

    public <T> T getter(String path, Class<T> responseType) {
        return get(path, responseType);
    }

    public <T> T getter(String path, TypeReference<T> responseType) {
        return get(path, responseType);
    }

    public void setter(String path, Object value) {
        setter(path, value, DEFAULT_SETTER_METHOD, Void.class);
    }

    public void setter(String path, Object value, JsonMethod method) {
        setter(path, value, method, Void.class);
    }

    public <T> T setter(String path, Object value, Class<T> responseType) {
        return setter(path, value, DEFAULT_SETTER_METHOD, responseType);
    }

    public <T> T setter(String path, Object value, JsonMethod method, Class<T> responseType) {
        Objects.requireNonNull(method, "method cannot be null");
        Objects.requireNonNull(responseType, "responseType cannot be null");
        if (!method.requestBodyRequired()) {
            throw new IllegalArgumentException("setter requires POST, PUT, or PATCH");
        }

        return switch (method) {
            case POST -> post(path, value, responseType);
            case PUT -> put(path, value, responseType);
            case PATCH -> patch(path, value, responseType);
            case GET -> throw new IllegalArgumentException("GET cannot be used as a setter");
        };
    }

    public <T> T setter(String path, Object value, TypeReference<T> responseType) {
        return setter(path, value, DEFAULT_SETTER_METHOD, responseType);
    }

    public <T> T setter(String path, Object value, JsonMethod method, TypeReference<T> responseType) {
        Objects.requireNonNull(method, "method cannot be null");
        Objects.requireNonNull(responseType, "responseType cannot be null");
        if (!method.requestBodyRequired()) {
            throw new IllegalArgumentException("setter requires POST, PUT, or PATCH");
        }

        return switch (method) {
            case POST -> post(path, value, responseType);
            case PUT -> put(path, value, responseType);
            case PATCH -> patch(path, value, responseType);
            case GET -> throw new IllegalArgumentException("GET cannot be used as a setter");
        };
    }

    public String setter(String path, String json, JsonMethod method) {
        Objects.requireNonNull(method, "method cannot be null");
        if (!method.requestBodyRequired()) {
            throw new IllegalArgumentException("setter requires POST, PUT, or PATCH");
        }
        JsonMapper.validate(json);
        return sendJson(method.httpMethod(), path, json);
    }

    public String request(String path, JsonMethod method, String json) {
        Objects.requireNonNull(method, "method cannot be null");
        if (method == JsonMethod.GET) {
            return getter(path);
        }
        return setter(path, json, method);
    }
}
