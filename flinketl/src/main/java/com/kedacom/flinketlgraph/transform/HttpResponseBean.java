package com.kedacom.flinketlgraph.transform;

public class HttpResponseBean {
    private int statudcode;
    private Object body;
    private Object extradata;

    public HttpResponseBean() {
    }

    public HttpResponseBean(int statudcode, Object body, Object extradata) {
        this.statudcode = statudcode;
        this.body = body;
        this.extradata = extradata;
    }

    @Override
    public String toString() {
        return "HttpResponseBean{" +
                "statudcode=" + statudcode +
                ", strbody='" + body + '\'' +
                ", extradata=" + extradata +
                '}';
    }

    public int getStatudcode() {
        return statudcode;
    }

    public void setStatudcode(int statudcode) {
        this.statudcode = statudcode;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object strbody) {
        this.body = strbody;
    }

    public Object getExtradata() {
        return extradata;
    }

    public void setExtradata(Object extradata) {
        this.extradata = extradata;
    }
}
