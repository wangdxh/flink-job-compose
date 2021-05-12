
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;


/**
 * some configs about processing txtfile.
 * 
 */
public class Linefileconfig implements Serializable
{

    /**
     * file encoding, such as utf-8, GB2312 ... if content type is lines, it need this config; if contenttype is structs msg content needs this config
     * 
     */
    private String encoding;
    /**
     * this config item will control the line number that filesystemcsourece operator will skip these lines when process this file the first time.
     * 
     */
    private Long skiplinenums = 0L;
    private final static long serialVersionUID = 113771172184L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Linefileconfig() {
    }

    /**
     * 
     * @param skiplinenums
     * @param encoding
     */
    public Linefileconfig(String encoding, Long skiplinenums) {
        super();
        this.encoding = encoding;
        this.skiplinenums = skiplinenums;
    }

    /**
     * file encoding, such as utf-8, GB2312 ... if content type is lines, it need this config; if contenttype is structs msg content needs this config
     * 
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * file encoding, such as utf-8, GB2312 ... if content type is lines, it need this config; if contenttype is structs msg content needs this config
     * 
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * this config item will control the line number that filesystemcsourece operator will skip these lines when process this file the first time.
     * 
     */
    public Long getSkiplinenums() {
        return skiplinenums;
    }

    /**
     * this config item will control the line number that filesystemcsourece operator will skip these lines when process this file the first time.
     * 
     */
    public void setSkiplinenums(Long skiplinenums) {
        this.skiplinenums = skiplinenums;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Linefileconfig.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("encoding");
        sb.append('=');
        sb.append(((this.encoding == null)?"<null>":this.encoding));
        sb.append(',');
        sb.append("skiplinenums");
        sb.append('=');
        sb.append(((this.skiplinenums == null)?"<null>":this.skiplinenums));
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
        result = ((result* 31)+((this.encoding == null)? 0 :this.encoding.hashCode()));
        result = ((result* 31)+((this.skiplinenums == null)? 0 :this.skiplinenums.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Linefileconfig) == false) {
            return false;
        }
        Linefileconfig rhs = ((Linefileconfig) other);
        return (((this.encoding == rhs.encoding)||((this.encoding!= null)&&this.encoding.equals(rhs.encoding)))&&((this.skiplinenums == rhs.skiplinenums)||((this.skiplinenums!= null)&&this.skiplinenums.equals(rhs.skiplinenums))));
    }

}
