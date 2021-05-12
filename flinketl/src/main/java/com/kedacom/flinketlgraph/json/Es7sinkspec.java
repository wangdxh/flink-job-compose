
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Es7sinkspec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    /**
     *  127.0.0.1:9200,192.168.1.2:9202
     * (Required)
     * 
     */
    private String hosts;
    private Long batchnums = 1000L;
    /**
     * timeunit is milliseconds, when this set batchnums will not working
     * 
     */
    private Long flushinterval = 1000L;
    /**
     * when u debug es, this is true, after debugging set this to false
     * 
     */
    private Boolean throwesexception = true;
    /**
     * es request retry times
     * 
     */
    private Long retries = 3L;
    private final static long serialVersionUID = 330088558959L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Es7sinkspec() {
    }

    /**
     * 
     * @param throwesexception
     * @param retries
     * @param batchnums
     * @param parallel
     * @param hosts
     * @param flushinterval
     */
    public Es7sinkspec(Long parallel, String hosts, Long batchnums, Long flushinterval, Boolean throwesexception, Long retries) {
        super();
        this.parallel = parallel;
        this.hosts = hosts;
        this.batchnums = batchnums;
        this.flushinterval = flushinterval;
        this.throwesexception = throwesexception;
        this.retries = retries;
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
     *  127.0.0.1:9200,192.168.1.2:9202
     * (Required)
     * 
     */
    public String getHosts() {
        return hosts;
    }

    /**
     *  127.0.0.1:9200,192.168.1.2:9202
     * (Required)
     * 
     */
    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public Long getBatchnums() {
        return batchnums;
    }

    public void setBatchnums(Long batchnums) {
        this.batchnums = batchnums;
    }

    /**
     * timeunit is milliseconds, when this set batchnums will not working
     * 
     */
    public Long getFlushinterval() {
        return flushinterval;
    }

    /**
     * timeunit is milliseconds, when this set batchnums will not working
     * 
     */
    public void setFlushinterval(Long flushinterval) {
        this.flushinterval = flushinterval;
    }

    /**
     * when u debug es, this is true, after debugging set this to false
     * 
     */
    public Boolean getThrowesexception() {
        return throwesexception;
    }

    /**
     * when u debug es, this is true, after debugging set this to false
     * 
     */
    public void setThrowesexception(Boolean throwesexception) {
        this.throwesexception = throwesexception;
    }

    /**
     * es request retry times
     * 
     */
    public Long getRetries() {
        return retries;
    }

    /**
     * es request retry times
     * 
     */
    public void setRetries(Long retries) {
        this.retries = retries;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Es7sinkspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("hosts");
        sb.append('=');
        sb.append(((this.hosts == null)?"<null>":this.hosts));
        sb.append(',');
        sb.append("batchnums");
        sb.append('=');
        sb.append(((this.batchnums == null)?"<null>":this.batchnums));
        sb.append(',');
        sb.append("flushinterval");
        sb.append('=');
        sb.append(((this.flushinterval == null)?"<null>":this.flushinterval));
        sb.append(',');
        sb.append("throwesexception");
        sb.append('=');
        sb.append(((this.throwesexception == null)?"<null>":this.throwesexception));
        sb.append(',');
        sb.append("retries");
        sb.append('=');
        sb.append(((this.retries == null)?"<null>":this.retries));
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
        result = ((result* 31)+((this.throwesexception == null)? 0 :this.throwesexception.hashCode()));
        result = ((result* 31)+((this.retries == null)? 0 :this.retries.hashCode()));
        result = ((result* 31)+((this.batchnums == null)? 0 :this.batchnums.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.hosts == null)? 0 :this.hosts.hashCode()));
        result = ((result* 31)+((this.flushinterval == null)? 0 :this.flushinterval.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Es7sinkspec) == false) {
            return false;
        }
        Es7sinkspec rhs = ((Es7sinkspec) other);
        return (((((((this.throwesexception == rhs.throwesexception)||((this.throwesexception!= null)&&this.throwesexception.equals(rhs.throwesexception)))&&((this.retries == rhs.retries)||((this.retries!= null)&&this.retries.equals(rhs.retries))))&&((this.batchnums == rhs.batchnums)||((this.batchnums!= null)&&this.batchnums.equals(rhs.batchnums))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.hosts == rhs.hosts)||((this.hosts!= null)&&this.hosts.equals(rhs.hosts))))&&((this.flushinterval == rhs.flushinterval)||((this.flushinterval!= null)&&this.flushinterval.equals(rhs.flushinterval))));
    }

}
