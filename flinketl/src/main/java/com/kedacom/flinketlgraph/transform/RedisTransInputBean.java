package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.sink.RedisBean;

import java.util.List;

public class RedisTransInputBean {
    List<RedisBean> redisinput;
    Object extradata;

    public RedisTransInputBean(List<RedisBean> redisinput, Object extradata) {
        this.redisinput = redisinput;
        this.extradata = extradata;
    }

    public RedisTransInputBean() {
    }

    public List<RedisBean> getRedisinput() {
        return redisinput;
    }

    public void setRedisinput(List<RedisBean> redisinput) {
        this.redisinput = redisinput;
    }

    public Object getExtradata() {
        return extradata;
    }

    public void setExtradata(Object extradata) {
        this.extradata = extradata;
    }

    @Override
    public String toString() {
        return "RedisTransInputBean{" +
                "redisinput=" + redisinput +
                ", extradata=" + extradata +
                '}';
    }
}
