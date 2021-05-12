
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Intervaljoinspec implements Serializable
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
    /**
     * lower bound, unit is milliseconds
     * 
     */
    private Long lowerbound = -5000L;
    /**
     * lower bound, unit is milliseconds
     * 
     */
    private Long upperbound = 5000L;
    private final static long serialVersionUID = 967258962872L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Intervaljoinspec() {
    }

    /**
     * 
     * @param leftkeyselector
     * @param parallel
     * @param rightkeyselector
     * @param lowerbound
     * @param upperbound
     */
    public Intervaljoinspec(Long parallel, String leftkeyselector, String rightkeyselector, Long lowerbound, Long upperbound) {
        super();
        this.parallel = parallel;
        this.leftkeyselector = leftkeyselector;
        this.rightkeyselector = rightkeyselector;
        this.lowerbound = lowerbound;
        this.upperbound = upperbound;
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

    /**
     * lower bound, unit is milliseconds
     * 
     */
    public Long getLowerbound() {
        return lowerbound;
    }

    /**
     * lower bound, unit is milliseconds
     * 
     */
    public void setLowerbound(Long lowerbound) {
        this.lowerbound = lowerbound;
    }

    /**
     * lower bound, unit is milliseconds
     * 
     */
    public Long getUpperbound() {
        return upperbound;
    }

    /**
     * lower bound, unit is milliseconds
     * 
     */
    public void setUpperbound(Long upperbound) {
        this.upperbound = upperbound;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Intervaljoinspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        sb.append("lowerbound");
        sb.append('=');
        sb.append(((this.lowerbound == null)?"<null>":this.lowerbound));
        sb.append(',');
        sb.append("upperbound");
        sb.append('=');
        sb.append(((this.upperbound == null)?"<null>":this.upperbound));
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
        result = ((result* 31)+((this.leftkeyselector == null)? 0 :this.leftkeyselector.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.rightkeyselector == null)? 0 :this.rightkeyselector.hashCode()));
        result = ((result* 31)+((this.lowerbound == null)? 0 :this.lowerbound.hashCode()));
        result = ((result* 31)+((this.upperbound == null)? 0 :this.upperbound.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Intervaljoinspec) == false) {
            return false;
        }
        Intervaljoinspec rhs = ((Intervaljoinspec) other);
        return ((((((this.leftkeyselector == rhs.leftkeyselector)||((this.leftkeyselector!= null)&&this.leftkeyselector.equals(rhs.leftkeyselector)))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.rightkeyselector == rhs.rightkeyselector)||((this.rightkeyselector!= null)&&this.rightkeyselector.equals(rhs.rightkeyselector))))&&((this.lowerbound == rhs.lowerbound)||((this.lowerbound!= null)&&this.lowerbound.equals(rhs.lowerbound))))&&((this.upperbound == rhs.upperbound)||((this.upperbound!= null)&&this.upperbound.equals(rhs.upperbound))));
    }

}
