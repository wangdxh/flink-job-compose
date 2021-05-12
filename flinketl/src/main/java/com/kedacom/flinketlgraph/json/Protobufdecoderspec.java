
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Protobufdecoderspec implements Serializable
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
    private List<Topictoprotojavaclas> topictoprotojavaclass = new ArrayList<Topictoprotojavaclas>();
    private final static long serialVersionUID = 10769739744L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Protobufdecoderspec() {
    }

    /**
     * 
     * @param topictoprotojavaclass
     * @param parallel
     */
    public Protobufdecoderspec(Long parallel, List<Topictoprotojavaclas> topictoprotojavaclass) {
        super();
        this.parallel = parallel;
        this.topictoprotojavaclass = topictoprotojavaclass;
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
    public List<Topictoprotojavaclas> getTopictoprotojavaclass() {
        return topictoprotojavaclass;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setTopictoprotojavaclass(List<Topictoprotojavaclas> topictoprotojavaclass) {
        this.topictoprotojavaclass = topictoprotojavaclass;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Protobufdecoderspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("topictoprotojavaclass");
        sb.append('=');
        sb.append(((this.topictoprotojavaclass == null)?"<null>":this.topictoprotojavaclass));
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
        result = ((result* 31)+((this.topictoprotojavaclass == null)? 0 :this.topictoprotojavaclass.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Protobufdecoderspec) == false) {
            return false;
        }
        Protobufdecoderspec rhs = ((Protobufdecoderspec) other);
        return (((this.topictoprotojavaclass == rhs.topictoprotojavaclass)||((this.topictoprotojavaclass!= null)&&this.topictoprotojavaclass.equals(rhs.topictoprotojavaclass)))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))));
    }

}
