
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Redistransformspec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    /**
     * when u debug http, this is true, after debugging set this to false
     * 
     */
    private Boolean throwexception = true;
    private Redissinkspec redisinfo;
    private final static long serialVersionUID = 345613692063L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Redistransformspec() {
    }

    /**
     * 
     * @param parallel
     * @param throwexception
     * @param redisinfo
     */
    public Redistransformspec(Long parallel, Boolean throwexception, Redissinkspec redisinfo) {
        super();
        this.parallel = parallel;
        this.throwexception = throwexception;
        this.redisinfo = redisinfo;
    }

    /**
     * 独立配置算子的并发度
     * 
     */
    public Long getParallel() {
        return parallel;
    }

    /**
     * 独立配置算子的并发度
     * 
     */
    public void setParallel(Long parallel) {
        this.parallel = parallel;
    }

    /**
     * when u debug http, this is true, after debugging set this to false
     * 
     */
    public Boolean getThrowexception() {
        return throwexception;
    }

    /**
     * when u debug http, this is true, after debugging set this to false
     * 
     */
    public void setThrowexception(Boolean throwexception) {
        this.throwexception = throwexception;
    }

    public Redissinkspec getRedisinfo() {
        return redisinfo;
    }

    public void setRedisinfo(Redissinkspec redisinfo) {
        this.redisinfo = redisinfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Redistransformspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("throwexception");
        sb.append('=');
        sb.append(((this.throwexception == null)?"<null>":this.throwexception));
        sb.append(',');
        sb.append("redisinfo");
        sb.append('=');
        sb.append(((this.redisinfo == null)?"<null>":this.redisinfo));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.throwexception == null)? 0 :this.throwexception.hashCode()));
        result = ((result* 31)+((this.redisinfo == null)? 0 :this.redisinfo.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Redistransformspec) == false) {
            return false;
        }
        Redistransformspec rhs = ((Redistransformspec) other);
        return ((((this.throwexception == rhs.throwexception)||((this.throwexception!= null)&&this.throwexception.equals(rhs.throwexception)))&&((this.redisinfo == rhs.redisinfo)||((this.redisinfo!= null)&&this.redisinfo.equals(rhs.redisinfo))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))));
    }

}
