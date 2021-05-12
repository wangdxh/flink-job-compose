
package com.kedacom.flinksql.json;

import java.io.Serializable;

public class Sqljobconfig implements Serializable
{

    /**
     * sqlconfig配置
     * (Required)
     * 
     */
    private Sqlconfig sqlconfig;
    /**
     * sql内容
     * 
     */
    private String sqlcontent;
    /**
     * sql文件路径
     * 
     */
    private String sqlfilepath;
    private final static long serialVersionUID = 5507789522607869346L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Sqljobconfig() {
    }

    /**
     * 
     * @param sqlconfig
     * @param sqlcontent
     * @param sqlfilepath
     */
    public Sqljobconfig(Sqlconfig sqlconfig, String sqlcontent, String sqlfilepath) {
        super();
        this.sqlconfig = sqlconfig;
        this.sqlcontent = sqlcontent;
        this.sqlfilepath = sqlfilepath;
    }

    /**
     * sqlconfig配置
     * (Required)
     * 
     */
    public Sqlconfig getSqlconfig() {
        return sqlconfig;
    }

    /**
     * sqlconfig配置
     * (Required)
     * 
     */
    public void setSqlconfig(Sqlconfig sqlconfig) {
        this.sqlconfig = sqlconfig;
    }

    /**
     * sql内容
     * 
     */
    public String getSqlcontent() {
        return sqlcontent;
    }

    /**
     * sql内容
     * 
     */
    public void setSqlcontent(String sqlcontent) {
        this.sqlcontent = sqlcontent;
    }

    /**
     * sql文件路径
     * 
     */
    public String getSqlfilepath() {
        return sqlfilepath;
    }

    /**
     * sql文件路径
     * 
     */
    public void setSqlfilepath(String sqlfilepath) {
        this.sqlfilepath = sqlfilepath;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Sqljobconfig.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("sqlconfig");
        sb.append('=');
        sb.append(((this.sqlconfig == null)?"<null>":this.sqlconfig));
        sb.append(',');
        sb.append("sqlcontent");
        sb.append('=');
        sb.append(((this.sqlcontent == null)?"<null>":this.sqlcontent));
        sb.append(',');
        sb.append("sqlfilepath");
        sb.append('=');
        sb.append(((this.sqlfilepath == null)?"<null>":this.sqlfilepath));
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
        result = ((result* 31)+((this.sqlconfig == null)? 0 :this.sqlconfig.hashCode()));
        result = ((result* 31)+((this.sqlcontent == null)? 0 :this.sqlcontent.hashCode()));
        result = ((result* 31)+((this.sqlfilepath == null)? 0 :this.sqlfilepath.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Sqljobconfig) == false) {
            return false;
        }
        Sqljobconfig rhs = ((Sqljobconfig) other);
        return ((((this.sqlconfig == rhs.sqlconfig)||((this.sqlconfig!= null)&&this.sqlconfig.equals(rhs.sqlconfig)))&&((this.sqlcontent == rhs.sqlcontent)||((this.sqlcontent!= null)&&this.sqlcontent.equals(rhs.sqlcontent))))&&((this.sqlfilepath == rhs.sqlfilepath)||((this.sqlfilepath!= null)&&this.sqlfilepath.equals(rhs.sqlfilepath))));
    }

}
