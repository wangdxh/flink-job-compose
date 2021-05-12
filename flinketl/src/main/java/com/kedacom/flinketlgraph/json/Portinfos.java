
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Portinfos implements Serializable
{

    private Elementportslist inputport;
    private Elementportslist outputport;
    private final static long serialVersionUID = 542589537889L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Portinfos() {
    }

    /**
     * 
     * @param inputport
     * @param outputport
     */
    public Portinfos(Elementportslist inputport, Elementportslist outputport) {
        super();
        this.inputport = inputport;
        this.outputport = outputport;
    }

    public Elementportslist getInputport() {
        return inputport;
    }

    public void setInputport(Elementportslist inputport) {
        this.inputport = inputport;
    }

    public Elementportslist getOutputport() {
        return outputport;
    }

    public void setOutputport(Elementportslist outputport) {
        this.outputport = outputport;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Portinfos.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("inputport");
        sb.append('=');
        sb.append(((this.inputport == null)?"<null>":this.inputport));
        sb.append(',');
        sb.append("outputport");
        sb.append('=');
        sb.append(((this.outputport == null)?"<null>":this.outputport));
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
        result = ((result* 31)+((this.inputport == null)? 0 :this.inputport.hashCode()));
        result = ((result* 31)+((this.outputport == null)? 0 :this.outputport.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Portinfos) == false) {
            return false;
        }
        Portinfos rhs = ((Portinfos) other);
        return (((this.inputport == rhs.inputport)||((this.inputport!= null)&&this.inputport.equals(rhs.inputport)))&&((this.outputport == rhs.outputport)||((this.outputport!= null)&&this.outputport.equals(rhs.outputport))));
    }

}
