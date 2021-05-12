
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Graphlink implements Serializable
{

    /**
     * 
     * (Required)
     * 
     */
    private Graphnodeport sourcenode;
    /**
     * 
     * (Required)
     * 
     */
    private Graphnodeport destnode;
    private final static long serialVersionUID = 561872453916L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Graphlink() {
    }

    /**
     * 
     * @param sourcenode
     * @param destnode
     */
    public Graphlink(Graphnodeport sourcenode, Graphnodeport destnode) {
        super();
        this.sourcenode = sourcenode;
        this.destnode = destnode;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Graphnodeport getSourcenode() {
        return sourcenode;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSourcenode(Graphnodeport sourcenode) {
        this.sourcenode = sourcenode;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Graphnodeport getDestnode() {
        return destnode;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setDestnode(Graphnodeport destnode) {
        this.destnode = destnode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Graphlink.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("sourcenode");
        sb.append('=');
        sb.append(((this.sourcenode == null)?"<null>":this.sourcenode));
        sb.append(',');
        sb.append("destnode");
        sb.append('=');
        sb.append(((this.destnode == null)?"<null>":this.destnode));
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
        result = ((result* 31)+((this.sourcenode == null)? 0 :this.sourcenode.hashCode()));
        result = ((result* 31)+((this.destnode == null)? 0 :this.destnode.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Graphlink) == false) {
            return false;
        }
        Graphlink rhs = ((Graphlink) other);
        return (((this.sourcenode == rhs.sourcenode)||((this.sourcenode!= null)&&this.sourcenode.equals(rhs.sourcenode)))&&((this.destnode == rhs.destnode)||((this.destnode!= null)&&this.destnode.equals(rhs.destnode))));
    }

}
