
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Unionspec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    private final static long serialVersionUID = 938260011442L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Unionspec() {
    }

    /**
     * 
     * @param parallel
     */
    public Unionspec(Long parallel) {
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
        sb.append(Unionspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        if ((other instanceof Unionspec) == false) {
            return false;
        }
        Unionspec rhs = ((Unionspec) other);
        return ((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel)));
    }

}
