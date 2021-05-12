
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Topictoprotojavaclas implements Serializable
{

    /**
     * 
     * (Required)
     * 
     */
    private String topicname;
    /**
     * protobuf message java classname
     * (Required)
     * 
     */
    private String protojavaclassname;
    private final static long serialVersionUID = 690057615428L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Topictoprotojavaclas() {
    }

    /**
     * 
     * @param topicname
     * @param protojavaclassname
     */
    public Topictoprotojavaclas(String topicname, String protojavaclassname) {
        super();
        this.topicname = topicname;
        this.protojavaclassname = protojavaclassname;
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
     * protobuf message java classname
     * (Required)
     * 
     */
    public String getProtojavaclassname() {
        return protojavaclassname;
    }

    /**
     * protobuf message java classname
     * (Required)
     * 
     */
    public void setProtojavaclassname(String protojavaclassname) {
        this.protojavaclassname = protojavaclassname;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Topictoprotojavaclas.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("topicname");
        sb.append('=');
        sb.append(((this.topicname == null)?"<null>":this.topicname));
        sb.append(',');
        sb.append("protojavaclassname");
        sb.append('=');
        sb.append(((this.protojavaclassname == null)?"<null>":this.protojavaclassname));
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
        result = ((result* 31)+((this.protojavaclassname == null)? 0 :this.protojavaclassname.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Topictoprotojavaclas) == false) {
            return false;
        }
        Topictoprotojavaclas rhs = ((Topictoprotojavaclas) other);
        return (((this.topicname == rhs.topicname)||((this.topicname!= null)&&this.topicname.equals(rhs.topicname)))&&((this.protojavaclassname == rhs.protojavaclassname)||((this.protojavaclassname!= null)&&this.protojavaclassname.equals(rhs.protojavaclassname))));
    }

}
