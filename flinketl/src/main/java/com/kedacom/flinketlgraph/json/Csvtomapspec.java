
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Csvtomapspec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    /**
     * the separator among cells within line, default value: half angle comma
     * 
     */
    private String splitstr = ",";
    /**
     * 
     * (Required)
     * 
     */
    private List<Columninfo> columninfo = new ArrayList<Columninfo>();
    private final static long serialVersionUID = 995996369873L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Csvtomapspec() {
    }

    /**
     * 
     * @param parallel
     * @param splitstr
     * @param columninfo
     */
    public Csvtomapspec(Long parallel, String splitstr, List<Columninfo> columninfo) {
        super();
        this.parallel = parallel;
        this.splitstr = splitstr;
        this.columninfo = columninfo;
    }

    /**
     * 独立配置算子的并发度
     * 
     */
    public Long getParallel() {
        return parallel;
    }

    /**
     * 独立配置算子的并发度
     * 
     */
    public void setParallel(Long parallel) {
        this.parallel = parallel;
    }

    /**
     * the separator among cells within line, default value: half angle comma
     * 
     */
    public String getSplitstr() {
        return splitstr;
    }

    /**
     * the separator among cells within line, default value: half angle comma
     * 
     */
    public void setSplitstr(String splitstr) {
        this.splitstr = splitstr;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Columninfo> getColumninfo() {
        return columninfo;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setColumninfo(List<Columninfo> columninfo) {
        this.columninfo = columninfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Csvtomapspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("splitstr");
        sb.append('=');
        sb.append(((this.splitstr == null)?"<null>":this.splitstr));
        sb.append(',');
        sb.append("columninfo");
        sb.append('=');
        sb.append(((this.columninfo == null)?"<null>":this.columninfo));
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
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.splitstr == null)? 0 :this.splitstr.hashCode()));
        result = ((result* 31)+((this.columninfo == null)? 0 :this.columninfo.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Csvtomapspec) == false) {
            return false;
        }
        Csvtomapspec rhs = ((Csvtomapspec) other);
        return ((((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel)))&&((this.splitstr == rhs.splitstr)||((this.splitstr!= null)&&this.splitstr.equals(rhs.splitstr))))&&((this.columninfo == rhs.columninfo)||((this.columninfo!= null)&&this.columninfo.equals(rhs.columninfo))));
    }

}
