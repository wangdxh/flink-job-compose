
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Elementportslist implements Serializable
{

    /**
     * port numbers
     * 
     */
    private Long portcount;
    private List<Elesingleportinfo> portlist = new ArrayList<Elesingleportinfo>();
    private final static long serialVersionUID = 688280657858L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Elementportslist() {
    }

    /**
     * 
     * @param portlist
     * @param portcount
     */
    public Elementportslist(Long portcount, List<Elesingleportinfo> portlist) {
        super();
        this.portcount = portcount;
        this.portlist = portlist;
    }

    /**
     * port numbers
     * 
     */
    public Long getPortcount() {
        return portcount;
    }

    /**
     * port numbers
     * 
     */
    public void setPortcount(Long portcount) {
        this.portcount = portcount;
    }

    public List<Elesingleportinfo> getPortlist() {
        return portlist;
    }

    public void setPortlist(List<Elesingleportinfo> portlist) {
        this.portlist = portlist;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Elementportslist.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("portcount");
        sb.append('=');
        sb.append(((this.portcount == null)?"<null>":this.portcount));
        sb.append(',');
        sb.append("portlist");
        sb.append('=');
        sb.append(((this.portlist == null)?"<null>":this.portlist));
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
        result = ((result* 31)+((this.portcount == null)? 0 :this.portcount.hashCode()));
        result = ((result* 31)+((this.portlist == null)? 0 :this.portlist.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Elementportslist) == false) {
            return false;
        }
        Elementportslist rhs = ((Elementportslist) other);
        return (((this.portcount == rhs.portcount)||((this.portcount!= null)&&this.portcount.equals(rhs.portcount)))&&((this.portlist == rhs.portlist)||((this.portlist!= null)&&this.portlist.equals(rhs.portlist))));
    }

}
