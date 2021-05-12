
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Graphnode implements Serializable
{

    /**
     * node id 在graph 内 是唯一的
     * (Required)
     * 
     */
    private String nodeid;
    /**
     * 对应的 算法element的名字
     * (Required)
     * 
     */
    private String elementname;
    /**
     * 每个算法element的详细配置
     * (Required)
     * 
     */
    private Object elementconfig;
    private List<Graphnodeport> inputselectedtypes = new ArrayList<Graphnodeport>();
    private List<Graphnodeport> outputselectedtypes = new ArrayList<Graphnodeport>();
    private final static long serialVersionUID = 756070323701L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Graphnode() {
    }

    /**
     * 
     * @param elementname
     * @param inputselectedtypes
     * @param outputselectedtypes
     * @param elementconfig
     * @param nodeid
     */
    public Graphnode(String nodeid, String elementname, Object elementconfig, List<Graphnodeport> inputselectedtypes, List<Graphnodeport> outputselectedtypes) {
        super();
        this.nodeid = nodeid;
        this.elementname = elementname;
        this.elementconfig = elementconfig;
        this.inputselectedtypes = inputselectedtypes;
        this.outputselectedtypes = outputselectedtypes;
    }

    /**
     * node id 在graph 内 是唯一的
     * (Required)
     * 
     */
    public String getNodeid() {
        return nodeid;
    }

    /**
     * node id 在graph 内 是唯一的
     * (Required)
     * 
     */
    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    /**
     * 对应的 算法element的名字
     * (Required)
     * 
     */
    public String getElementname() {
        return elementname;
    }

    /**
     * 对应的 算法element的名字
     * (Required)
     * 
     */
    public void setElementname(String elementname) {
        this.elementname = elementname;
    }

    /**
     * 每个算法element的详细配置
     * (Required)
     * 
     */
    public Object getElementconfig() {
        return elementconfig;
    }

    /**
     * 每个算法element的详细配置
     * (Required)
     * 
     */
    public void setElementconfig(Object elementconfig) {
        this.elementconfig = elementconfig;
    }

    public List<Graphnodeport> getInputselectedtypes() {
        return inputselectedtypes;
    }

    public void setInputselectedtypes(List<Graphnodeport> inputselectedtypes) {
        this.inputselectedtypes = inputselectedtypes;
    }

    public List<Graphnodeport> getOutputselectedtypes() {
        return outputselectedtypes;
    }

    public void setOutputselectedtypes(List<Graphnodeport> outputselectedtypes) {
        this.outputselectedtypes = outputselectedtypes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Graphnode.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("nodeid");
        sb.append('=');
        sb.append(((this.nodeid == null)?"<null>":this.nodeid));
        sb.append(',');
        sb.append("elementname");
        sb.append('=');
        sb.append(((this.elementname == null)?"<null>":this.elementname));
        sb.append(',');
        sb.append("elementconfig");
        sb.append('=');
        sb.append(((this.elementconfig == null)?"<null>":this.elementconfig));
        sb.append(',');
        sb.append("inputselectedtypes");
        sb.append('=');
        sb.append(((this.inputselectedtypes == null)?"<null>":this.inputselectedtypes));
        sb.append(',');
        sb.append("outputselectedtypes");
        sb.append('=');
        sb.append(((this.outputselectedtypes == null)?"<null>":this.outputselectedtypes));
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
        result = ((result* 31)+((this.elementconfig == null)? 0 :this.elementconfig.hashCode()));
        result = ((result* 31)+((this.elementname == null)? 0 :this.elementname.hashCode()));
        result = ((result* 31)+((this.inputselectedtypes == null)? 0 :this.inputselectedtypes.hashCode()));
        result = ((result* 31)+((this.outputselectedtypes == null)? 0 :this.outputselectedtypes.hashCode()));
        result = ((result* 31)+((this.nodeid == null)? 0 :this.nodeid.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Graphnode) == false) {
            return false;
        }
        Graphnode rhs = ((Graphnode) other);
        return ((((((this.elementconfig == rhs.elementconfig)||((this.elementconfig!= null)&&this.elementconfig.equals(rhs.elementconfig)))&&((this.elementname == rhs.elementname)||((this.elementname!= null)&&this.elementname.equals(rhs.elementname))))&&((this.inputselectedtypes == rhs.inputselectedtypes)||((this.inputselectedtypes!= null)&&this.inputselectedtypes.equals(rhs.inputselectedtypes))))&&((this.outputselectedtypes == rhs.outputselectedtypes)||((this.outputselectedtypes!= null)&&this.outputselectedtypes.equals(rhs.outputselectedtypes))))&&((this.nodeid == rhs.nodeid)||((this.nodeid!= null)&&this.nodeid.equals(rhs.nodeid))));
    }

}
