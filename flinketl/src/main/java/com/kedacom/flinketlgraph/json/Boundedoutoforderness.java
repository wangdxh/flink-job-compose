
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Boundedoutoforderness implements Serializable
{

    /**
     * max outof orderness unit is mill seconds
     * 
     */
    private Long maxoutoforderness = 100L;
    /**
     * extracetimestamp from map object, this is the key name
     * 
     */
    private String extracttimestamp;
    private final static long serialVersionUID = 265592030492L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Boundedoutoforderness() {
    }

    /**
     * 
     * @param extracttimestamp
     * @param maxoutoforderness
     */
    public Boundedoutoforderness(Long maxoutoforderness, String extracttimestamp) {
        super();
        this.maxoutoforderness = maxoutoforderness;
        this.extracttimestamp = extracttimestamp;
    }

    /**
     * max outof orderness unit is mill seconds
     * 
     */
    public Long getMaxoutoforderness() {
        return maxoutoforderness;
    }

    /**
     * max outof orderness unit is mill seconds
     * 
     */
    public void setMaxoutoforderness(Long maxoutoforderness) {
        this.maxoutoforderness = maxoutoforderness;
    }

    /**
     * extracetimestamp from map object, this is the key name
     * 
     */
    public String getExtracttimestamp() {
        return extracttimestamp;
    }

    /**
     * extracetimestamp from map object, this is the key name
     * 
     */
    public void setExtracttimestamp(String extracttimestamp) {
        this.extracttimestamp = extracttimestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Boundedoutoforderness.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("maxoutoforderness");
        sb.append('=');
        sb.append(((this.maxoutoforderness == null)?"<null>":this.maxoutoforderness));
        sb.append(',');
        sb.append("extracttimestamp");
        sb.append('=');
        sb.append(((this.extracttimestamp == null)?"<null>":this.extracttimestamp));
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
        result = ((result* 31)+((this.extracttimestamp == null)? 0 :this.extracttimestamp.hashCode()));
        result = ((result* 31)+((this.maxoutoforderness == null)? 0 :this.maxoutoforderness.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Boundedoutoforderness) == false) {
            return false;
        }
        Boundedoutoforderness rhs = ((Boundedoutoforderness) other);
        return (((this.extracttimestamp == rhs.extracttimestamp)||((this.extracttimestamp!= null)&&this.extracttimestamp.equals(rhs.extracttimestamp)))&&((this.maxoutoforderness == rhs.maxoutoforderness)||((this.maxoutoforderness!= null)&&this.maxoutoforderness.equals(rhs.maxoutoforderness))));
    }

}
