package com.kedacom.flinketlgraph.transform;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequestBean {
    private Map<String, String> headers = new LinkedHashMap<>();
    private String url;
    private Object body;
    private Object extradata;
    private String method = "post";
    private String bodytype = "string";    // string, bytearray

    public HttpRequestBean() {
    }

    public HttpRequestBean(Map<String, String> headers, String url, Object body, Object extradata) {
        this.headers = headers;
        this.url = url;
        this.body = body;
        this.extradata = extradata;
    }

    public HttpRequestBean(Map<String, String> headers, String url, Object body, Object extradata, String method) {
        this(headers, url, body, extradata);
        this.method = method;
    }
    @Override
    public String toString() {
        return "HttpRequestBean{" +
                "headers=" + headers +
                ", url='" + url + '\'' +
                ", body=" + body +
                ", extradata=" + extradata +
                ", method=" + method +
                ", bodytype=" + bodytype +
                '}';
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Object getExtradata() {
        return extradata;
    }

    public void setExtradata(Object extradata) {
        this.extradata = extradata;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBodytype() {
        return bodytype;
    }

    public void setBodytype(String bodytype) {
        this.bodytype = bodytype;
    }
}
