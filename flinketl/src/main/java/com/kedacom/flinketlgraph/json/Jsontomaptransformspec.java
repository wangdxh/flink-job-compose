
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Jsontomaptransformspec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    private final static long serialVersionUID = 234370936758L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Jsontomaptransformspec() {
    }

    /**
     * 
     * @param parallel
     */
    public Jsontomaptransformspec(Long parallel) {
        super();
        this.parallel = parallel;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Jsontomaptransformspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
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
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Jsontomaptransformspec) == false) {
            return false;
        }
        Jsontomaptransformspec rhs = ((Jsontomaptransformspec) other);
        return ((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel)));
    }

}
