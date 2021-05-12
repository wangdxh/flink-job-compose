
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;


/**
 * checkpoint的配置
 * 
 */
public class Chkpointcfg implements Serializable
{

    /**
     * 是否开启checkpoint
     * 
     */
    private Boolean enable = true;
    /**
     * checkpoint 时间间隔
     * 
     */
    private Long interval = 120L;
    /**
     * checkpoint 超时时间
     * 
     */
    private Long timeout = 600L;
    /**
     * 数据处理模式是否是精确一次，false表示至少一次
     * 
     */
    private Boolean exactlyonce = true;
    private final static long serialVersionUID = 350700822921L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Chkpointcfg() {
    }

    /**
     * 
     * @param enable
     * @param exactlyonce
     * @param interval
     * @param timeout
     */
    public Chkpointcfg(Boolean enable, Long interval, Long timeout, Boolean exactlyonce) {
        super();
        this.enable = enable;
        this.interval = interval;
        this.timeout = timeout;
        this.exactlyonce = exactlyonce;
    }

    /**
     * 是否开启checkpoint
     * 
     */
    public Boolean getEnable() {
        return enable;
    }

    /**
     * 是否开启checkpoint
     * 
     */
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    /**
     * checkpoint 时间间隔
     * 
     */
    public Long getInterval() {
        return interval;
    }

    /**
     * checkpoint 时间间隔
     * 
     */
    public void setInterval(Long interval) {
        this.interval = interval;
    }

    /**
     * checkpoint 超时时间
     * 
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * checkpoint 超时时间
     * 
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    /**
     * 数据处理模式是否是精确一次，false表示至少一次
     * 
     */
    public Boolean getExactlyonce() {
        return exactlyonce;
    }

    /**
     * 数据处理模式是否是精确一次，false表示至少一次
     * 
     */
    public void setExactlyonce(Boolean exactlyonce) {
        this.exactlyonce = exactlyonce;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Chkpointcfg.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("enable");
        sb.append('=');
        sb.append(((this.enable == null)?"<null>":this.enable));
        sb.append(',');
        sb.append("interval");
        sb.append('=');
        sb.append(((this.interval == null)?"<null>":this.interval));
        sb.append(',');
        sb.append("timeout");
        sb.append('=');
        sb.append(((this.timeout == null)?"<null>":this.timeout));
        sb.append(',');
        sb.append("exactlyonce");
        sb.append('=');
        sb.append(((this.exactlyonce == null)?"<null>":this.exactlyonce));
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
        result = ((result* 31)+((this.exactlyonce == null)? 0 :this.exactlyonce.hashCode()));
        result = ((result* 31)+((this.interval == null)? 0 :this.interval.hashCode()));
        result = ((result* 31)+((this.enable == null)? 0 :this.enable.hashCode()));
        result = ((result* 31)+((this.timeout == null)? 0 :this.timeout.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Chkpointcfg) == false) {
            return false;
        }
        Chkpointcfg rhs = ((Chkpointcfg) other);
        return (((((this.exactlyonce == rhs.exactlyonce)||((this.exactlyonce!= null)&&this.exactlyonce.equals(rhs.exactlyonce)))&&((this.interval == rhs.interval)||((this.interval!= null)&&this.interval.equals(rhs.interval))))&&((this.enable == rhs.enable)||((this.enable!= null)&&this.enable.equals(rhs.enable))))&&((this.timeout == rhs.timeout)||((this.timeout!= null)&&this.timeout.equals(rhs.timeout))));
    }

}
