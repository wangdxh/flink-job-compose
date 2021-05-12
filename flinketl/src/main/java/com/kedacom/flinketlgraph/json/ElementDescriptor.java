
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class ElementDescriptor implements Serializable
{

    private String elementname;
    private String desc;
    /**
     * service | job | algorithm  | complexalgorithm
     * 
     */
    private String elementtype;
    /**
     * flink | rest
     * 
     */
    private String enginetype;
    /**
     * source | transform | sink
     * 
     */
    private String enginecaptype;
    /**
     * 配置的元数据信息
     * 
     */
    private Object elementconfigschema;
    private Portinfos portinfos;
    private final static long serialVersionUID = 610902641492L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ElementDescriptor() {
    }

    /**
     * 
     * @param elementname
     * @param elementconfigschema
     * @param enginecaptype
     * @param portinfos
     * @param desc
     * @param elementtype
     * @param enginetype
     */
    public ElementDescriptor(String elementname, String desc, String elementtype, String enginetype, String enginecaptype, Object elementconfigschema, Portinfos portinfos) {
        super();
        this.elementname = elementname;
        this.desc = desc;
        this.elementtype = elementtype;
        this.enginetype = enginetype;
        this.enginecaptype = enginecaptype;
        this.elementconfigschema = elementconfigschema;
        this.portinfos = portinfos;
    }

    public String getElementname() {
        return elementname;
    }

    public void setElementname(String elementname) {
        this.elementname = elementname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * service | job | algorithm  | complexalgorithm
     * 
     */
    public String getElementtype() {
        return elementtype;
    }

    /**
     * service | job | algorithm  | complexalgorithm
     * 
     */
    public void setElementtype(String elementtype) {
        this.elementtype = elementtype;
    }

    /**
     * flink | rest
     * 
     */
    public String getEnginetype() {
        return enginetype;
    }

    /**
     * flink | rest
     * 
     */
    public void setEnginetype(String enginetype) {
        this.enginetype = enginetype;
    }

    /**
     * source | transform | sink
     * 
     */
    public String getEnginecaptype() {
        return enginecaptype;
    }

    /**
     * source | transform | sink
     * 
     */
    public void setEnginecaptype(String enginecaptype) {
        this.enginecaptype = enginecaptype;
    }

    /**
     * 配置的元数据信息
     * 
     */
    public Object getElementconfigschema() {
        return elementconfigschema;
    }

    /**
     * 配置的元数据信息
     * 
     */
    public void setElementconfigschema(Object elementconfigschema) {
        this.elementconfigschema = elementconfigschema;
    }

    public Portinfos getPortinfos() {
        return portinfos;
    }

    public void setPortinfos(Portinfos portinfos) {
        this.portinfos = portinfos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ElementDescriptor.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("elementname");
        sb.append('=');
        sb.append(((this.elementname == null)?"<null>":this.elementname));
        sb.append(',');
        sb.append("desc");
        sb.append('=');
        sb.append(((this.desc == null)?"<null>":this.desc));
        sb.append(',');
        sb.append("elementtype");
        sb.append('=');
        sb.append(((this.elementtype == null)?"<null>":this.elementtype));
        sb.append(',');
        sb.append("enginetype");
        sb.append('=');
        sb.append(((this.enginetype == null)?"<null>":this.enginetype));
        sb.append(',');
        sb.append("enginecaptype");
        sb.append('=');
        sb.append(((this.enginecaptype == null)?"<null>":this.enginecaptype));
        sb.append(',');
        sb.append("elementconfigschema");
        sb.append('=');
        sb.append(((this.elementconfigschema == null)?"<null>":this.elementconfigschema));
        sb.append(',');
        sb.append("portinfos");
        sb.append('=');
        sb.append(((this.portinfos == null)?"<null>":this.portinfos));
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
        result = ((result* 31)+((this.elementname == null)? 0 :this.elementname.hashCode()));
        result = ((result* 31)+((this.elementconfigschema == null)? 0 :this.elementconfigschema.hashCode()));
        result = ((result* 31)+((this.enginecaptype == null)? 0 :this.enginecaptype.hashCode()));
        result = ((result* 31)+((this.portinfos == null)? 0 :this.portinfos.hashCode()));
        result = ((result* 31)+((this.desc == null)? 0 :this.desc.hashCode()));
        result = ((result* 31)+((this.elementtype == null)? 0 :this.elementtype.hashCode()));
        result = ((result* 31)+((this.enginetype == null)? 0 :this.enginetype.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ElementDescriptor) == false) {
            return false;
        }
        ElementDescriptor rhs = ((ElementDescriptor) other);
        return ((((((((this.elementname == rhs.elementname)||((this.elementname!= null)&&this.elementname.equals(rhs.elementname)))&&((this.elementconfigschema == rhs.elementconfigschema)||((this.elementconfigschema!= null)&&this.elementconfigschema.equals(rhs.elementconfigschema))))&&((this.enginecaptype == rhs.enginecaptype)||((this.enginecaptype!= null)&&this.enginecaptype.equals(rhs.enginecaptype))))&&((this.portinfos == rhs.portinfos)||((this.portinfos!= null)&&this.portinfos.equals(rhs.portinfos))))&&((this.desc == rhs.desc)||((this.desc!= null)&&this.desc.equals(rhs.desc))))&&((this.elementtype == rhs.elementtype)||((this.elementtype!= null)&&this.elementtype.equals(rhs.elementtype))))&&((this.enginetype == rhs.enginetype)||((this.enginetype!= null)&&this.enginetype.equals(rhs.enginetype))));
    }

}
