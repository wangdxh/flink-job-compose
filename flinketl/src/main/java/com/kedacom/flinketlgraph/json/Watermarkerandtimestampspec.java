
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Watermarkerandtimestampspec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    /**
     * the type of the flink system, default is process time, most is event time
     * 
     */
    private Watermarkerandtimestampspec.Timetype timetype = Watermarkerandtimestampspec.Timetype.fromValue("PROCESSTIME");
    /**
     * Periodic Watermarks default is 0, when event time should set bigger than 0 
     * 
     */
    private Long watermarkinterval = 0L;
    private Boundedoutoforderness boundedoutoforderness;
    private final static long serialVersionUID = 1050497097150L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Watermarkerandtimestampspec() {
    }

    /**
     * 
     * @param watermarkinterval
     * @param parallel
     * @param timetype
     * @param boundedoutoforderness
     */
    public Watermarkerandtimestampspec(Long parallel, Watermarkerandtimestampspec.Timetype timetype, Long watermarkinterval, Boundedoutoforderness boundedoutoforderness) {
        super();
        this.parallel = parallel;
        this.timetype = timetype;
        this.watermarkinterval = watermarkinterval;
        this.boundedoutoforderness = boundedoutoforderness;
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
     * the type of the flink system, default is process time, most is event time
     * 
     */
    public Watermarkerandtimestampspec.Timetype getTimetype() {
        return timetype;
    }

    /**
     * the type of the flink system, default is process time, most is event time
     * 
     */
    public void setTimetype(Watermarkerandtimestampspec.Timetype timetype) {
        this.timetype = timetype;
    }

    /**
     * Periodic Watermarks default is 0, when event time should set bigger than 0 
     * 
     */
    public Long getWatermarkinterval() {
        return watermarkinterval;
    }

    /**
     * Periodic Watermarks default is 0, when event time should set bigger than 0 
     * 
     */
    public void setWatermarkinterval(Long watermarkinterval) {
        this.watermarkinterval = watermarkinterval;
    }

    public Boundedoutoforderness getBoundedoutoforderness() {
        return boundedoutoforderness;
    }

    public void setBoundedoutoforderness(Boundedoutoforderness boundedoutoforderness) {
        this.boundedoutoforderness = boundedoutoforderness;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Watermarkerandtimestampspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("timetype");
        sb.append('=');
        sb.append(((this.timetype == null)?"<null>":this.timetype));
        sb.append(',');
        sb.append("watermarkinterval");
        sb.append('=');
        sb.append(((this.watermarkinterval == null)?"<null>":this.watermarkinterval));
        sb.append(',');
        sb.append("boundedoutoforderness");
        sb.append('=');
        sb.append(((this.boundedoutoforderness == null)?"<null>":this.boundedoutoforderness));
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
        result = ((result* 31)+((this.timetype == null)? 0 :this.timetype.hashCode()));
        result = ((result* 31)+((this.watermarkinterval == null)? 0 :this.watermarkinterval.hashCode()));
        result = ((result* 31)+((this.boundedoutoforderness == null)? 0 :this.boundedoutoforderness.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Watermarkerandtimestampspec) == false) {
            return false;
        }
        Watermarkerandtimestampspec rhs = ((Watermarkerandtimestampspec) other);
        return (((((this.timetype == rhs.timetype)||((this.timetype!= null)&&this.timetype.equals(rhs.timetype)))&&((this.watermarkinterval == rhs.watermarkinterval)||((this.watermarkinterval!= null)&&this.watermarkinterval.equals(rhs.watermarkinterval))))&&((this.boundedoutoforderness == rhs.boundedoutoforderness)||((this.boundedoutoforderness!= null)&&this.boundedoutoforderness.equals(rhs.boundedoutoforderness))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))));
    }

    public enum Timetype {

        EVENTTIME("EVENTTIME"),
        PROCESSTIME("PROCESSTIME");
        private final String value;
        private final static Map<String, Watermarkerandtimestampspec.Timetype> CONSTANTS = new HashMap<String, Watermarkerandtimestampspec.Timetype>();

        static {
            for (Watermarkerandtimestampspec.Timetype c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Timetype(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Watermarkerandtimestampspec.Timetype fromValue(String value) {
            Watermarkerandtimestampspec.Timetype constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
