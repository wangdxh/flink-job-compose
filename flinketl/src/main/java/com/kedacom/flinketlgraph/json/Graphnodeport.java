
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Graphnodeport implements Serializable
{

    /**
     * 
     * (Required)
     * 
     */
    private String nodeid;
    /**
     * 
     * (Required)
     * 
     */
    private Long portindex;
    /**
     * 
     * (Required)
     * 
     */
    private String portselectedtype;
    private final static long serialVersionUID = 162879854747L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Graphnodeport() {
    }

    /**
     * 
     * @param portselectedtype
     * @param portindex
     * @param nodeid
     */
    public Graphnodeport(String nodeid, Long portindex, String portselectedtype) {
        super();
        this.nodeid = nodeid;
        this.portindex = portindex;
        this.portselectedtype = portselectedtype;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getNodeid() {
        return nodeid;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Long getPortindex() {
        return portindex;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setPortindex(Long portindex) {
        this.portindex = portindex;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getPortselectedtype() {
        return portselectedtype;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setPortselectedtype(String portselectedtype) {
        this.portselectedtype = portselectedtype;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Graphnodeport.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("nodeid");
        sb.append('=');
        sb.append(((this.nodeid == null)?"<null>":this.nodeid));
        sb.append(',');
        sb.append("portindex");
        sb.append('=');
        sb.append(((this.portindex == null)?"<null>":this.portindex));
        sb.append(',');
        sb.append("portselectedtype");
        sb.append('=');
        sb.append(((this.portselectedtype == null)?"<null>":this.portselectedtype));
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
        result = ((result* 31)+((this.portselectedtype == null)? 0 :this.portselectedtype.hashCode()));
        result = ((result* 31)+((this.nodeid == null)? 0 :this.nodeid.hashCode()));
        result = ((result* 31)+((this.portindex == null)? 0 :this.portindex.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Graphnodeport) == false) {
            return false;
        }
        Graphnodeport rhs = ((Graphnodeport) other);
        return ((((this.portselectedtype == rhs.portselectedtype)||((this.portselectedtype!= null)&&this.portselectedtype.equals(rhs.portselectedtype)))&&((this.nodeid == rhs.nodeid)||((this.nodeid!= null)&&this.nodeid.equals(rhs.nodeid))))&&((this.portindex == rhs.portindex)||((this.portindex!= null)&&this.portindex.equals(rhs.portindex))));
    }

}
