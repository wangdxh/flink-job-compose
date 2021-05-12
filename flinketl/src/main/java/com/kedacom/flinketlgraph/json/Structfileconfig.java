
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * some configs about struct byte files.
 * 
 */
public class Structfileconfig implements Serializable
{

    /**
     * support 2 or 4.only when contenttype is structs this item is needed; this config stands for the length of msg.
     * 
     */
    private Long lengthbytes = 4L;
    /**
     * endian mode, big, little, default value big-endian as well as jvm
     * 
     */
    private Structfileconfig.Endian endian = Structfileconfig.Endian.fromValue("BIG");
    /**
     * the size limit of per msg, unit Byte. stop to process current file when finding one msg size great than msgsizelimit.
     * 
     */
    private Long msgsizelimit = 1048576L;
    private final static long serialVersionUID = 747159899355L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Structfileconfig() {
    }

    /**
     * 
     * @param msgsizelimit
     * @param lengthbytes
     * @param endian
     */
    public Structfileconfig(Long lengthbytes, Structfileconfig.Endian endian, Long msgsizelimit) {
        super();
        this.lengthbytes = lengthbytes;
        this.endian = endian;
        this.msgsizelimit = msgsizelimit;
    }

    /**
     * support 2 or 4.only when contenttype is structs this item is needed; this config stands for the length of msg.
     * 
     */
    public Long getLengthbytes() {
        return lengthbytes;
    }

    /**
     * support 2 or 4.only when contenttype is structs this item is needed; this config stands for the length of msg.
     * 
     */
    public void setLengthbytes(Long lengthbytes) {
        this.lengthbytes = lengthbytes;
    }

    /**
     * endian mode, big, little, default value big-endian as well as jvm
     * 
     */
    public Structfileconfig.Endian getEndian() {
        return endian;
    }

    /**
     * endian mode, big, little, default value big-endian as well as jvm
     * 
     */
    public void setEndian(Structfileconfig.Endian endian) {
        this.endian = endian;
    }

    /**
     * the size limit of per msg, unit Byte. stop to process current file when finding one msg size great than msgsizelimit.
     * 
     */
    public Long getMsgsizelimit() {
        return msgsizelimit;
    }

    /**
     * the size limit of per msg, unit Byte. stop to process current file when finding one msg size great than msgsizelimit.
     * 
     */
    public void setMsgsizelimit(Long msgsizelimit) {
        this.msgsizelimit = msgsizelimit;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Structfileconfig.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("lengthbytes");
        sb.append('=');
        sb.append(((this.lengthbytes == null)?"<null>":this.lengthbytes));
        sb.append(',');
        sb.append("endian");
        sb.append('=');
        sb.append(((this.endian == null)?"<null>":this.endian));
        sb.append(',');
        sb.append("msgsizelimit");
        sb.append('=');
        sb.append(((this.msgsizelimit == null)?"<null>":this.msgsizelimit));
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
        result = ((result* 31)+((this.lengthbytes == null)? 0 :this.lengthbytes.hashCode()));
        result = ((result* 31)+((this.msgsizelimit == null)? 0 :this.msgsizelimit.hashCode()));
        result = ((result* 31)+((this.endian == null)? 0 :this.endian.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Structfileconfig) == false) {
            return false;
        }
        Structfileconfig rhs = ((Structfileconfig) other);
        return ((((this.lengthbytes == rhs.lengthbytes)||((this.lengthbytes!= null)&&this.lengthbytes.equals(rhs.lengthbytes)))&&((this.msgsizelimit == rhs.msgsizelimit)||((this.msgsizelimit!= null)&&this.msgsizelimit.equals(rhs.msgsizelimit))))&&((this.endian == rhs.endian)||((this.endian!= null)&&this.endian.equals(rhs.endian))));
    }

    public enum Endian {

        BIG("BIG"),
        LITTLE("LITTLE");
        private final String value;
        private final static Map<String, Structfileconfig.Endian> CONSTANTS = new HashMap<String, Structfileconfig.Endian>();

        static {
            for (Structfileconfig.Endian c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Endian(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Structfileconfig.Endian fromValue(String value) {
            Structfileconfig.Endian constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
