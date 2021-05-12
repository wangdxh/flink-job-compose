
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Protobufjsonnodespec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    private String descfilepath;
    private String descfilecontent;
    /**
     * 
     * (Required)
     * 
     */
    private List<Topictoprotoclas> topictoprotoclass = new ArrayList<Topictoprotoclas>();
    private final static long serialVersionUID = 872483372350L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Protobufjsonnodespec() {
    }

    /**
     * 
     * @param parallel
     * @param descfilepath
     * @param topictoprotoclass
     * @param descfilecontent
     */
    public Protobufjsonnodespec(Long parallel, String descfilepath, String descfilecontent, List<Topictoprotoclas> topictoprotoclass) {
        super();
        this.parallel = parallel;
        this.descfilepath = descfilepath;
        this.descfilecontent = descfilecontent;
        this.topictoprotoclass = topictoprotoclass;
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

    public String getDescfilepath() {
        return descfilepath;
    }

    public void setDescfilepath(String descfilepath) {
        this.descfilepath = descfilepath;
    }

    public String getDescfilecontent() {
        return descfilecontent;
    }

    public void setDescfilecontent(String descfilecontent) {
        this.descfilecontent = descfilecontent;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Topictoprotoclas> getTopictoprotoclass() {
        return topictoprotoclass;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setTopictoprotoclass(List<Topictoprotoclas> topictoprotoclass) {
        this.topictoprotoclass = topictoprotoclass;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Protobufjsonnodespec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("descfilepath");
        sb.append('=');
        sb.append(((this.descfilepath == null)?"<null>":this.descfilepath));
        sb.append(',');
        sb.append("descfilecontent");
        sb.append('=');
        sb.append(((this.descfilecontent == null)?"<null>":this.descfilecontent));
        sb.append(',');
        sb.append("topictoprotoclass");
        sb.append('=');
        sb.append(((this.topictoprotoclass == null)?"<null>":this.topictoprotoclass));
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
        result = ((result* 31)+((this.descfilepath == null)? 0 :this.descfilepath.hashCode()));
        result = ((result* 31)+((this.topictoprotoclass == null)? 0 :this.topictoprotoclass.hashCode()));
        result = ((result* 31)+((this.descfilecontent == null)? 0 :this.descfilecontent.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Protobufjsonnodespec) == false) {
            return false;
        }
        Protobufjsonnodespec rhs = ((Protobufjsonnodespec) other);
        return (((((this.descfilepath == rhs.descfilepath)||((this.descfilepath!= null)&&this.descfilepath.equals(rhs.descfilepath)))&&((this.topictoprotoclass == rhs.topictoprotoclass)||((this.topictoprotoclass!= null)&&this.topictoprotoclass.equals(rhs.topictoprotoclass))))&&((this.descfilecontent == rhs.descfilecontent)||((this.descfilecontent!= null)&&this.descfilecontent.equals(rhs.descfilecontent))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))));
    }

}
