
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Filesystemsourcespec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    /**
     * the top directory contained target files.
     * (Required)
     * 
     */
    private String basedirectory;
    /**
     * filename wildcard character. this config can be null
     * 
     */
    private String filterrule = "*";
    /**
     * whether deletes file processed; default value false, don't delete it.
     * 
     */
    private Boolean delete = false;
    /**
     * the type of file content has tow kinds, lines and structs. 
     * 
     */
    private Filesystemsourcespec.Contenttype contenttype = Filesystemsourcespec.Contenttype.fromValue("LINES");
    /**
     * some configs about processing txtfile.
     * 
     */
    private Linefileconfig linefileconfig;
    /**
     * some configs about struct byte files.
     * 
     */
    private Structfileconfig structfileconfig;
    private final static long serialVersionUID = 501369438961L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Filesystemsourcespec() {
    }

    /**
     * 
     * @param basedirectory
     * @param linefileconfig
     * @param parallel
     * @param structfileconfig
     * @param delete
     * @param filterrule
     * @param contenttype
     */
    public Filesystemsourcespec(Long parallel, String basedirectory, String filterrule, Boolean delete, Filesystemsourcespec.Contenttype contenttype, Linefileconfig linefileconfig, Structfileconfig structfileconfig) {
        super();
        this.parallel = parallel;
        this.basedirectory = basedirectory;
        this.filterrule = filterrule;
        this.delete = delete;
        this.contenttype = contenttype;
        this.linefileconfig = linefileconfig;
        this.structfileconfig = structfileconfig;
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
     * the top directory contained target files.
     * (Required)
     * 
     */
    public String getBasedirectory() {
        return basedirectory;
    }

    /**
     * the top directory contained target files.
     * (Required)
     * 
     */
    public void setBasedirectory(String basedirectory) {
        this.basedirectory = basedirectory;
    }

    /**
     * filename wildcard character. this config can be null
     * 
     */
    public String getFilterrule() {
        return filterrule;
    }

    /**
     * filename wildcard character. this config can be null
     * 
     */
    public void setFilterrule(String filterrule) {
        this.filterrule = filterrule;
    }

    /**
     * whether deletes file processed; default value false, don't delete it.
     * 
     */
    public Boolean getDelete() {
        return delete;
    }

    /**
     * whether deletes file processed; default value false, don't delete it.
     * 
     */
    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    /**
     * the type of file content has tow kinds, lines and structs. 
     * 
     */
    public Filesystemsourcespec.Contenttype getContenttype() {
        return contenttype;
    }

    /**
     * the type of file content has tow kinds, lines and structs. 
     * 
     */
    public void setContenttype(Filesystemsourcespec.Contenttype contenttype) {
        this.contenttype = contenttype;
    }

    /**
     * some configs about processing txtfile.
     * 
     */
    public Linefileconfig getLinefileconfig() {
        return linefileconfig;
    }

    /**
     * some configs about processing txtfile.
     * 
     */
    public void setLinefileconfig(Linefileconfig linefileconfig) {
        this.linefileconfig = linefileconfig;
    }

    /**
     * some configs about struct byte files.
     * 
     */
    public Structfileconfig getStructfileconfig() {
        return structfileconfig;
    }

    /**
     * some configs about struct byte files.
     * 
     */
    public void setStructfileconfig(Structfileconfig structfileconfig) {
        this.structfileconfig = structfileconfig;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Filesystemsourcespec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("basedirectory");
        sb.append('=');
        sb.append(((this.basedirectory == null)?"<null>":this.basedirectory));
        sb.append(',');
        sb.append("filterrule");
        sb.append('=');
        sb.append(((this.filterrule == null)?"<null>":this.filterrule));
        sb.append(',');
        sb.append("delete");
        sb.append('=');
        sb.append(((this.delete == null)?"<null>":this.delete));
        sb.append(',');
        sb.append("contenttype");
        sb.append('=');
        sb.append(((this.contenttype == null)?"<null>":this.contenttype));
        sb.append(',');
        sb.append("linefileconfig");
        sb.append('=');
        sb.append(((this.linefileconfig == null)?"<null>":this.linefileconfig));
        sb.append(',');
        sb.append("structfileconfig");
        sb.append('=');
        sb.append(((this.structfileconfig == null)?"<null>":this.structfileconfig));
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
        result = ((result* 31)+((this.basedirectory == null)? 0 :this.basedirectory.hashCode()));
        result = ((result* 31)+((this.linefileconfig == null)? 0 :this.linefileconfig.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.structfileconfig == null)? 0 :this.structfileconfig.hashCode()));
        result = ((result* 31)+((this.delete == null)? 0 :this.delete.hashCode()));
        result = ((result* 31)+((this.filterrule == null)? 0 :this.filterrule.hashCode()));
        result = ((result* 31)+((this.contenttype == null)? 0 :this.contenttype.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Filesystemsourcespec) == false) {
            return false;
        }
        Filesystemsourcespec rhs = ((Filesystemsourcespec) other);
        return ((((((((this.basedirectory == rhs.basedirectory)||((this.basedirectory!= null)&&this.basedirectory.equals(rhs.basedirectory)))&&((this.linefileconfig == rhs.linefileconfig)||((this.linefileconfig!= null)&&this.linefileconfig.equals(rhs.linefileconfig))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.structfileconfig == rhs.structfileconfig)||((this.structfileconfig!= null)&&this.structfileconfig.equals(rhs.structfileconfig))))&&((this.delete == rhs.delete)||((this.delete!= null)&&this.delete.equals(rhs.delete))))&&((this.filterrule == rhs.filterrule)||((this.filterrule!= null)&&this.filterrule.equals(rhs.filterrule))))&&((this.contenttype == rhs.contenttype)||((this.contenttype!= null)&&this.contenttype.equals(rhs.contenttype))));
    }

    public enum Contenttype {

        LINES("LINES"),
        STRUCTS("STRUCTS");
        private final String value;
        private final static Map<String, Filesystemsourcespec.Contenttype> CONSTANTS = new HashMap<String, Filesystemsourcespec.Contenttype>();

        static {
            for (Filesystemsourcespec.Contenttype c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Contenttype(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Filesystemsourcespec.Contenttype fromValue(String value) {
            Filesystemsourcespec.Contenttype constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
