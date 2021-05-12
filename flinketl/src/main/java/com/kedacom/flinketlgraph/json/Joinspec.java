
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Joinspec implements Serializable
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
    private String leftkeyselector;
    /**
     * 
     * (Required)
     * 
     */
    private String rightkeyselector;
    private Windowinfo windowinfo;
    private final static long serialVersionUID = 579373938588L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Joinspec() {
    }

    /**
     * 
     * @param windowinfo
     * @param leftkeyselector
     * @param parallel
     * @param rightkeyselector
     */
    public Joinspec(Long parallel, String leftkeyselector, String rightkeyselector, Windowinfo windowinfo) {
        super();
        this.parallel = parallel;
        this.leftkeyselector = leftkeyselector;
        this.rightkeyselector = rightkeyselector;
        this.windowinfo = windowinfo;
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
    public String getLeftkeyselector() {
        return leftkeyselector;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setLeftkeyselector(String leftkeyselector) {
        this.leftkeyselector = leftkeyselector;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getRightkeyselector() {
        return rightkeyselector;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setRightkeyselector(String rightkeyselector) {
        this.rightkeyselector = rightkeyselector;
    }

    public Windowinfo getWindowinfo() {
        return windowinfo;
    }

    public void setWindowinfo(Windowinfo windowinfo) {
        this.windowinfo = windowinfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Joinspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("leftkeyselector");
        sb.append('=');
        sb.append(((this.leftkeyselector == null)?"<null>":this.leftkeyselector));
        sb.append(',');
        sb.append("rightkeyselector");
        sb.append('=');
        sb.append(((this.rightkeyselector == null)?"<null>":this.rightkeyselector));
        sb.append(',');
        sb.append("windowinfo");
        sb.append('=');
        sb.append(((this.windowinfo == null)?"<null>":this.windowinfo));
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
        result = ((result* 31)+((this.windowinfo == null)? 0 :this.windowinfo.hashCode()));
        result = ((result* 31)+((this.leftkeyselector == null)? 0 :this.leftkeyselector.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.rightkeyselector == null)? 0 :this.rightkeyselector.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Joinspec) == false) {
            return false;
        }
        Joinspec rhs = ((Joinspec) other);
        return (((((this.windowinfo == rhs.windowinfo)||((this.windowinfo!= null)&&this.windowinfo.equals(rhs.windowinfo)))&&((this.leftkeyselector == rhs.leftkeyselector)||((this.leftkeyselector!= null)&&this.leftkeyselector.equals(rhs.leftkeyselector))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.rightkeyselector == rhs.rightkeyselector)||((this.rightkeyselector!= null)&&this.rightkeyselector.equals(rhs.rightkeyselector))));
    }

}
