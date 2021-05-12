
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Topictoprotoclas implements Serializable
{

    /**
     * 
     * (Required)
     * 
     */
    private String topicname;
    /**
     * 
     * (Required)
     * 
     */
    private String protoclassname;
    private final static long serialVersionUID = 251596874685L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Topictoprotoclas() {
    }

    /**
     * 
     * @param protoclassname
     * @param topicname
     */
    public Topictoprotoclas(String topicname, String protoclassname) {
        super();
        this.topicname = topicname;
        this.protoclassname = protoclassname;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getTopicname() {
        return topicname;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setTopicname(String topicname) {
        this.topicname = topicname;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getProtoclassname() {
        return protoclassname;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setProtoclassname(String protoclassname) {
        this.protoclassname = protoclassname;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Topictoprotoclas.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("topicname");
        sb.append('=');
        sb.append(((this.topicname == null)?"<null>":this.topicname));
        sb.append(',');
        sb.append("protoclassname");
        sb.append('=');
        sb.append(((this.protoclassname == null)?"<null>":this.protoclassname));
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
        result = ((result* 31)+((this.topicname == null)? 0 :this.topicname.hashCode()));
        result = ((result* 31)+((this.protoclassname == null)? 0 :this.protoclassname.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Topictoprotoclas) == false) {
            return false;
        }
        Topictoprotoclas rhs = ((Topictoprotoclas) other);
        return (((this.topicname == rhs.topicname)||((this.topicname!= null)&&this.topicname.equals(rhs.topicname)))&&((this.protoclassname == rhs.protoclassname)||((this.protoclassname!= null)&&this.protoclassname.equals(rhs.protoclassname))));
    }

}
