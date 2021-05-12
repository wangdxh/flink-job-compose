
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Columninfo implements Serializable
{

    /**
     * the index of element within line, starting from 0
     * (Required)
     * 
     */
    private Long colindex;
    /**
     * column name of element within line, used to construct a map object
     * (Required)
     * 
     */
    private String colname;
    /**
     * the data type of element, support String, Long, Integer, Float
     * (Required)
     * 
     */
    private Columninfo.Coltype coltype;
    private final static long serialVersionUID = 478758749764L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Columninfo() {
    }

    /**
     * 
     * @param coltype
     * @param colname
     * @param colindex
     */
    public Columninfo(Long colindex, String colname, Columninfo.Coltype coltype) {
        super();
        this.colindex = colindex;
        this.colname = colname;
        this.coltype = coltype;
    }

    /**
     * the index of element within line, starting from 0
     * (Required)
     * 
     */
    public Long getColindex() {
        return colindex;
    }

    /**
     * the index of element within line, starting from 0
     * (Required)
     * 
     */
    public void setColindex(Long colindex) {
        this.colindex = colindex;
    }

    /**
     * column name of element within line, used to construct a map object
     * (Required)
     * 
     */
    public String getColname() {
        return colname;
    }

    /**
     * column name of element within line, used to construct a map object
     * (Required)
     * 
     */
    public void setColname(String colname) {
        this.colname = colname;
    }

    /**
     * the data type of element, support String, Long, Integer, Float
     * (Required)
     * 
     */
    public Columninfo.Coltype getColtype() {
        return coltype;
    }

    /**
     * the data type of element, support String, Long, Integer, Float
     * (Required)
     * 
     */
    public void setColtype(Columninfo.Coltype coltype) {
        this.coltype = coltype;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Columninfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("colindex");
        sb.append('=');
        sb.append(((this.colindex == null)?"<null>":this.colindex));
        sb.append(',');
        sb.append("colname");
        sb.append('=');
        sb.append(((this.colname == null)?"<null>":this.colname));
        sb.append(',');
        sb.append("coltype");
        sb.append('=');
        sb.append(((this.coltype == null)?"<null>":this.coltype));
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
        result = ((result* 31)+((this.coltype == null)? 0 :this.coltype.hashCode()));
        result = ((result* 31)+((this.colname == null)? 0 :this.colname.hashCode()));
        result = ((result* 31)+((this.colindex == null)? 0 :this.colindex.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Columninfo) == false) {
            return false;
        }
        Columninfo rhs = ((Columninfo) other);
        return ((((this.coltype == rhs.coltype)||((this.coltype!= null)&&this.coltype.equals(rhs.coltype)))&&((this.colname == rhs.colname)||((this.colname!= null)&&this.colname.equals(rhs.colname))))&&((this.colindex == rhs.colindex)||((this.colindex!= null)&&this.colindex.equals(rhs.colindex))));
    }

    public enum Coltype {

        STRING("STRING"),
        LONG("LONG"),
        INTEGER("INTEGER"),
        FLOAT("FLOAT");
        private final String value;
        private final static Map<String, Columninfo.Coltype> CONSTANTS = new HashMap<String, Columninfo.Coltype>();

        static {
            for (Columninfo.Coltype c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Coltype(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Columninfo.Coltype fromValue(String value) {
            Columninfo.Coltype constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
