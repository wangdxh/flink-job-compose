
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Windowinfo implements Serializable
{

    private Windowinfo.Windowtype windowtype = Windowinfo.Windowtype.fromValue("SLIDING");
    /**
     * unit is millisecond
     * 
     */
    private Long size = 0L;
    /**
     * unit is millisecond just in sliding
     * 
     */
    private Long slide = 0L;
    /**
     * unit is millisecond
     * 
     */
    private Long offset = 0L;
    /**
     * unit is millisecond
     * 
     */
    private Long allowedLateness = 0L;
    private final static long serialVersionUID = 723429506757L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Windowinfo() {
    }

    /**
     * 
     * @param size
     * @param offset
     * @param allowedLateness
     * @param slide
     * @param windowtype
     */
    public Windowinfo(Windowinfo.Windowtype windowtype, Long size, Long slide, Long offset, Long allowedLateness) {
        super();
        this.windowtype = windowtype;
        this.size = size;
        this.slide = slide;
        this.offset = offset;
        this.allowedLateness = allowedLateness;
    }

    public Windowinfo.Windowtype getWindowtype() {
        return windowtype;
    }

    public void setWindowtype(Windowinfo.Windowtype windowtype) {
        this.windowtype = windowtype;
    }

    /**
     * unit is millisecond
     * 
     */
    public Long getSize() {
        return size;
    }

    /**
     * unit is millisecond
     * 
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * unit is millisecond just in sliding
     * 
     */
    public Long getSlide() {
        return slide;
    }

    /**
     * unit is millisecond just in sliding
     * 
     */
    public void setSlide(Long slide) {
        this.slide = slide;
    }

    /**
     * unit is millisecond
     * 
     */
    public Long getOffset() {
        return offset;
    }

    /**
     * unit is millisecond
     * 
     */
    public void setOffset(Long offset) {
        this.offset = offset;
    }

    /**
     * unit is millisecond
     * 
     */
    public Long getAllowedLateness() {
        return allowedLateness;
    }

    /**
     * unit is millisecond
     * 
     */
    public void setAllowedLateness(Long allowedLateness) {
        this.allowedLateness = allowedLateness;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Windowinfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("windowtype");
        sb.append('=');
        sb.append(((this.windowtype == null)?"<null>":this.windowtype));
        sb.append(',');
        sb.append("size");
        sb.append('=');
        sb.append(((this.size == null)?"<null>":this.size));
        sb.append(',');
        sb.append("slide");
        sb.append('=');
        sb.append(((this.slide == null)?"<null>":this.slide));
        sb.append(',');
        sb.append("offset");
        sb.append('=');
        sb.append(((this.offset == null)?"<null>":this.offset));
        sb.append(',');
        sb.append("allowedLateness");
        sb.append('=');
        sb.append(((this.allowedLateness == null)?"<null>":this.allowedLateness));
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
        result = ((result* 31)+((this.windowtype == null)? 0 :this.windowtype.hashCode()));
        result = ((result* 31)+((this.size == null)? 0 :this.size.hashCode()));
        result = ((result* 31)+((this.offset == null)? 0 :this.offset.hashCode()));
        result = ((result* 31)+((this.allowedLateness == null)? 0 :this.allowedLateness.hashCode()));
        result = ((result* 31)+((this.slide == null)? 0 :this.slide.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Windowinfo) == false) {
            return false;
        }
        Windowinfo rhs = ((Windowinfo) other);
        return ((((((this.windowtype == rhs.windowtype)||((this.windowtype!= null)&&this.windowtype.equals(rhs.windowtype)))&&((this.size == rhs.size)||((this.size!= null)&&this.size.equals(rhs.size))))&&((this.offset == rhs.offset)||((this.offset!= null)&&this.offset.equals(rhs.offset))))&&((this.allowedLateness == rhs.allowedLateness)||((this.allowedLateness!= null)&&this.allowedLateness.equals(rhs.allowedLateness))))&&((this.slide == rhs.slide)||((this.slide!= null)&&this.slide.equals(rhs.slide))));
    }

    public enum Windowtype {

        TUMBLING("TUMBLING"),
        SLIDING("SLIDING");
        private final String value;
        private final static Map<String, Windowinfo.Windowtype> CONSTANTS = new HashMap<String, Windowinfo.Windowtype>();

        static {
            for (Windowinfo.Windowtype c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Windowtype(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Windowinfo.Windowtype fromValue(String value) {
            Windowinfo.Windowtype constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
