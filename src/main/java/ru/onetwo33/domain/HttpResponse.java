package ru.onetwo33.domain;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private String statusCode;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public HttpResponse() {
    }

    public HttpResponse(String statusCode, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeaders(String key, String value) {
        this.headers.put(key, value);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode='" + statusCode + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
