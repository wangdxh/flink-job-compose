package com.kedacom.flinketlgraph.sink;

public class RedisBean {

    public enum Command {
        SADD, SET, ZADD, HSET,
        GET,HGET,HGETALL,SISMEMBER
    }

    private String command;
    private String additionalkey;
    private String key;
    private String value;

    public RedisBean() {
    }

    public RedisBean(String command, String key , String additionalkey, String value) {
        this.command = command;
        this.additionalkey = additionalkey;
        this.key = key;
        this.value = value;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getAdditionalkey() {
        return additionalkey;
    }

    public void setAdditionalkey(String additionalkey) {
        this.additionalkey = additionalkey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
