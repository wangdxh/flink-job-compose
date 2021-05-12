
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Sideouttag implements Serializable
{

    /**
     * 
     * (Required)
     * 
     */
    private Long outputportindex;
    /**
     * 
     * (Required)
     * 
     */
    private String tagname;
    private final static long serialVersionUID = 1051544104282L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Sideouttag() {
    }

    /**
     * 
     * @param tagname
     * @param outputportindex
     */
    public Sideouttag(Long outputportindex, String tagname) {
        super();
        this.outputportindex = outputportindex;
        this.tagname = tagname;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Long getOutputportindex() {
        return outputportindex;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setOutputportindex(Long outputportindex) {
        this.outputportindex = outputportindex;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getTagname() {
        return tagname;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Sideouttag.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("outputportindex");
        sb.append('=');
        sb.append(((this.outputportindex == null)?"<null>":this.outputportindex));
        sb.append(',');
        sb.append("tagname");
        sb.append('=');
        sb.append(((this.tagname == null)?"<null>":this.tagname));
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
        result = ((result* 31)+((this.tagname == null)? 0 :this.tagname.hashCode()));
        result = ((result* 31)+((this.outputportindex == null)? 0 :this.outputportindex.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Sideouttag) == false) {
            return false;
        }
        Sideouttag rhs = ((Sideouttag) other);
        return (((this.tagname == rhs.tagname)||((this.tagname!= null)&&this.tagname.equals(rhs.tagname)))&&((this.outputportindex == rhs.outputportindex)||((this.outputportindex!= null)&&this.outputportindex.equals(rhs.outputportindex))));
    }

}
