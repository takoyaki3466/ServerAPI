package org.takoyaki.json;

import org.takoyaki.http.HttpMethod;

public enum JsonMethod {
    GET(HttpMethod.GET, false),
    POST(HttpMethod.POST, true),
    PUT(HttpMethod.PUT, true),
    PATCH(HttpMethod.PATCH, true);

    private final HttpMethod httpMethod;
    private final boolean requestBodyRequired;

    JsonMethod(HttpMethod httpMethod, boolean requestBodyRequired) {
        this.httpMethod = httpMethod;
        this.requestBodyRequired = requestBodyRequired;
    }

    public HttpMethod httpMethod() {
        return httpMethod;
    }

    public boolean requestBodyRequired() {
        return requestBodyRequired;
    }
}
