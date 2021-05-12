
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Filelinessourcespec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    /**
     * 
     * (Required)
     * 
     */
    private String filepath;
    /**
     * lines interval sleep millseconds
     * 
     */
    private Long linesinterval = 0L;
    private final static long serialVersionUID = 632581968902L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Filelinessourcespec() {
    }

    /**
     * 
     * @param filepath
     * @param parallel
     * @param linesinterval
     */
    public Filelinessourcespec(Long parallel, String filepath, Long linesinterval) {
        super();
        this.parallel = parallel;
        this.filepath = filepath;
        this.linesinterval = linesinterval;
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
     * 
     * (Required)
     * 
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * lines interval sleep millseconds
     * 
     */
    public Long getLinesinterval() {
        return linesinterval;
    }

    /**
     * lines interval sleep millseconds
     * 
     */
    public void setLinesinterval(Long linesinterval) {
        this.linesinterval = linesinterval;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Filelinessourcespec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("filepath");
        sb.append('=');
        sb.append(((this.filepath == null)?"<null>":this.filepath));
        sb.append(',');
        sb.append("linesinterval");
        sb.append('=');
        sb.append(((this.linesinterval == null)?"<null>":this.linesinterval));
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
        result = ((result* 31)+((this.linesinterval == null)? 0 :this.linesinterval.hashCode()));
        result = ((result* 31)+((this.filepath == null)? 0 :this.filepath.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Filelinessourcespec) == false) {
            return false;
        }
        Filelinessourcespec rhs = ((Filelinessourcespec) other);
        return ((((this.linesinterval == rhs.linesinterval)||((this.linesinterval!= null)&&this.linesinterval.equals(rhs.linesinterval)))&&((this.filepath == rhs.filepath)||((this.filepath!= null)&&this.filepath.equals(rhs.filepath))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))));
    }

}
