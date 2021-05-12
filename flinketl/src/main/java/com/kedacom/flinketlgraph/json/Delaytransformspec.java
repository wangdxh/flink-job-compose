
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Delaytransformspec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    /**
     * delay time, unit is second, default is 10 second
     * 
     */
    private Long delayedtime = 10L;
    private final static long serialVersionUID = 211821768263L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Delaytransformspec() {
    }

    /**
     * 
     * @param parallel
     * @param delayedtime
     */
    public Delaytransformspec(Long parallel, Long delayedtime) {
        super();
        this.parallel = parallel;
        this.delayedtime = delayedtime;
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
     * delay time, unit is second, default is 10 second
     * 
     */
    public Long getDelayedtime() {
        return delayedtime;
    }

    /**
     * delay time, unit is second, default is 10 second
     * 
     */
    public void setDelayedtime(Long delayedtime) {
        this.delayedtime = delayedtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Delaytransformspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("delayedtime");
        sb.append('=');
        sb.append(((this.delayedtime == null)?"<null>":this.delayedtime));
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
        result = ((result* 31)+((this.delayedtime == null)? 0 :this.delayedtime.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Delaytransformspec) == false) {
            return false;
        }
        Delaytransformspec rhs = ((Delaytransformspec) other);
        return (((this.delayedtime == rhs.delayedtime)||((this.delayedtime!= null)&&this.delayedtime.equals(rhs.delayedtime)))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))));
    }

}
