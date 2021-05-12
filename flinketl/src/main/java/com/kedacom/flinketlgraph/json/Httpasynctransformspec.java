
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Httpasynctransformspec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    private List<Httpheader> httpheaders = new ArrayList<Httpheader>();
    private Long httpconcurrency = 100L;
    /**
     * http request retry times
     * 
     */
    private Long retries = 3L;
    /**
     * when u debug http, this is true, after debugging set this to false
     * 
     */
    private Boolean throwhttpexception = true;
    /**
     * tcp connect time out seconds
     * 
     */
    private Long connecttimeout = 5L;
    /**
     * when tcp socket no data or server no response, will timeout
     * 
     */
    private Long sockettimeout = 10L;
    private final static long serialVersionUID = 872973394236L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Httpasynctransformspec() {
    }

    /**
     * 
     * @param retries
     * @param connecttimeout
     * @param httpheaders
     * @param sockettimeout
     * @param parallel
     * @param throwhttpexception
     * @param httpconcurrency
     */
    public Httpasynctransformspec(Long parallel, List<Httpheader> httpheaders, Long httpconcurrency, Long retries, Boolean throwhttpexception, Long connecttimeout, Long sockettimeout) {
        super();
        this.parallel = parallel;
        this.httpheaders = httpheaders;
        this.httpconcurrency = httpconcurrency;
        this.retries = retries;
        this.throwhttpexception = throwhttpexception;
        this.connecttimeout = connecttimeout;
        this.sockettimeout = sockettimeout;
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

    public List<Httpheader> getHttpheaders() {
        return httpheaders;
    }

    public void setHttpheaders(List<Httpheader> httpheaders) {
        this.httpheaders = httpheaders;
    }

    public Long getHttpconcurrency() {
        return httpconcurrency;
    }

    public void setHttpconcurrency(Long httpconcurrency) {
        this.httpconcurrency = httpconcurrency;
    }

    /**
     * http request retry times
     * 
     */
    public Long getRetries() {
        return retries;
    }

    /**
     * http request retry times
     * 
     */
    public void setRetries(Long retries) {
        this.retries = retries;
    }

    /**
     * when u debug http, this is true, after debugging set this to false
     * 
     */
    public Boolean getThrowhttpexception() {
        return throwhttpexception;
    }

    /**
     * when u debug http, this is true, after debugging set this to false
     * 
     */
    public void setThrowhttpexception(Boolean throwhttpexception) {
        this.throwhttpexception = throwhttpexception;
    }

    /**
     * tcp connect time out seconds
     * 
     */
    public Long getConnecttimeout() {
        return connecttimeout;
    }

    /**
     * tcp connect time out seconds
     * 
     */
    public void setConnecttimeout(Long connecttimeout) {
        this.connecttimeout = connecttimeout;
    }

    /**
     * when tcp socket no data or server no response, will timeout
     * 
     */
    public Long getSockettimeout() {
        return sockettimeout;
    }

    /**
     * when tcp socket no data or server no response, will timeout
     * 
     */
    public void setSockettimeout(Long sockettimeout) {
        this.sockettimeout = sockettimeout;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Httpasynctransformspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("httpheaders");
        sb.append('=');
        sb.append(((this.httpheaders == null)?"<null>":this.httpheaders));
        sb.append(',');
        sb.append("httpconcurrency");
        sb.append('=');
        sb.append(((this.httpconcurrency == null)?"<null>":this.httpconcurrency));
        sb.append(',');
        sb.append("retries");
        sb.append('=');
        sb.append(((this.retries == null)?"<null>":this.retries));
        sb.append(',');
        sb.append("throwhttpexception");
        sb.append('=');
        sb.append(((this.throwhttpexception == null)?"<null>":this.throwhttpexception));
        sb.append(',');
        sb.append("connecttimeout");
        sb.append('=');
        sb.append(((this.connecttimeout == null)?"<null>":this.connecttimeout));
        sb.append(',');
        sb.append("sockettimeout");
        sb.append('=');
        sb.append(((this.sockettimeout == null)?"<null>":this.sockettimeout));
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
        result = ((result* 31)+((this.retries == null)? 0 :this.retries.hashCode()));
        result = ((result* 31)+((this.connecttimeout == null)? 0 :this.connecttimeout.hashCode()));
        result = ((result* 31)+((this.httpheaders == null)? 0 :this.httpheaders.hashCode()));
        result = ((result* 31)+((this.sockettimeout == null)? 0 :this.sockettimeout.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.throwhttpexception == null)? 0 :this.throwhttpexception.hashCode()));
        result = ((result* 31)+((this.httpconcurrency == null)? 0 :this.httpconcurrency.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Httpasynctransformspec) == false) {
            return false;
        }
        Httpasynctransformspec rhs = ((Httpasynctransformspec) other);
        return ((((((((this.retries == rhs.retries)||((this.retries!= null)&&this.retries.equals(rhs.retries)))&&((this.connecttimeout == rhs.connecttimeout)||((this.connecttimeout!= null)&&this.connecttimeout.equals(rhs.connecttimeout))))&&((this.httpheaders == rhs.httpheaders)||((this.httpheaders!= null)&&this.httpheaders.equals(rhs.httpheaders))))&&((this.sockettimeout == rhs.sockettimeout)||((this.sockettimeout!= null)&&this.sockettimeout.equals(rhs.sockettimeout))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.throwhttpexception == rhs.throwhttpexception)||((this.throwhttpexception!= null)&&this.throwhttpexception.equals(rhs.throwhttpexception))))&&((this.httpconcurrency == rhs.httpconcurrency)||((this.httpconcurrency!= null)&&this.httpconcurrency.equals(rhs.httpconcurrency))));
    }

}
