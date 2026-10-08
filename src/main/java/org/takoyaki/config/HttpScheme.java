package org.takoyaki.config;

public enum HttpScheme {
    HTTP("http"),
    HTTPS("https");

    private final String value;

    HttpScheme(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
