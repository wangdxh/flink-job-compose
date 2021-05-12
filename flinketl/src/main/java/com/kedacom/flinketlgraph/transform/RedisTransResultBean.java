package com.kedacom.flinketlgraph.transform;

import java.util.LinkedList;
import java.util.List;

public class RedisTransResultBean {
    List<Object> redisresult = new LinkedList<>();
    Object extradata = null;

    public RedisTransResultBean(List<Object> redisresult, Object extradata) {
        this.redisresult = redisresult;
        this.extradata = extradata;
    }

    public RedisTransResultBean() {
    }

    public List<Object> getRedisresult() {
        return redisresult;
    }

    public void setRedisresult(List<Object> redisresult) {
        this.redisresult = redisresult;
    }

    public Object getExtradata() {
        return extradata;
    }

    public void setExtradata(Object extradata) {
        this.extradata = extradata;
    }

    @Override
    public String toString() {
        return "RedisTransResultBean{" +
                "redisresult=" + redisresult +
                ", extradata=" + extradata +
                '}';
    }
}
