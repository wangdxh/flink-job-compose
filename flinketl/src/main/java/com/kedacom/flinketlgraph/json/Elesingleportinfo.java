
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Elesingleportinfo implements Serializable
{

    /**
     * port index
     * 
     */
    private Long index;
    private List<String> supporttypes = new ArrayList<String>();
    /**
     * port 的名称，用于显示在界面上
     * 
     */
    private String portname;
    /**
     * port 的描述信息
     * 
     */
    private String portdesc;
    private final static long serialVersionUID = 705855022029L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Elesingleportinfo() {
    }

    /**
     * 
     * @param supporttypes
     * @param portdesc
     * @param index
     * @param portname
     */
    public Elesingleportinfo(Long index, List<String> supporttypes, String portname, String portdesc) {
        super();
        this.index = index;
        this.supporttypes = supporttypes;
        this.portname = portname;
        this.portdesc = portdesc;
    }

    /**
     * port index
     * 
     */
    public Long getIndex() {
        return index;
    }

    /**
     * port index
     * 
     */
    public void setIndex(Long index) {
        this.index = index;
    }

    public List<String> getSupporttypes() {
        return supporttypes;
    }

    public void setSupporttypes(List<String> supporttypes) {
        this.supporttypes = supporttypes;
    }

    /**
     * port 的名称，用于显示在界面上
     * 
     */
    public String getPortname() {
        return portname;
    }

    /**
     * port 的名称，用于显示在界面上
     * 
     */
    public void setPortname(String portname) {
        this.portname = portname;
    }

    /**
     * port 的描述信息
     * 
     */
    public String getPortdesc() {
        return portdesc;
    }

    /**
     * port 的描述信息
     * 
     */
    public void setPortdesc(String portdesc) {
        this.portdesc = portdesc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Elesingleportinfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("index");
        sb.append('=');
        sb.append(((this.index == null)?"<null>":this.index));
        sb.append(',');
        sb.append("supporttypes");
        sb.append('=');
        sb.append(((this.supporttypes == null)?"<null>":this.supporttypes));
        sb.append(',');
        sb.append("portname");
        sb.append('=');
        sb.append(((this.portname == null)?"<null>":this.portname));
        sb.append(',');
        sb.append("portdesc");
        sb.append('=');
        sb.append(((this.portdesc == null)?"<null>":this.portdesc));
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
        result = ((result* 31)+((this.index == null)? 0 :this.index.hashCode()));
        result = ((result* 31)+((this.supporttypes == null)? 0 :this.supporttypes.hashCode()));
        result = ((result* 31)+((this.portname == null)? 0 :this.portname.hashCode()));
        result = ((result* 31)+((this.portdesc == null)? 0 :this.portdesc.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Elesingleportinfo) == false) {
            return false;
        }
        Elesingleportinfo rhs = ((Elesingleportinfo) other);
        return (((((this.index == rhs.index)||((this.index!= null)&&this.index.equals(rhs.index)))&&((this.supporttypes == rhs.supporttypes)||((this.supporttypes!= null)&&this.supporttypes.equals(rhs.supporttypes))))&&((this.portname == rhs.portname)||((this.portname!= null)&&this.portname.equals(rhs.portname))))&&((this.portdesc == rhs.portdesc)||((this.portdesc!= null)&&this.portdesc.equals(rhs.portdesc))));
    }

}
