
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Jdbcsourcespec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    /**
     * jdbc:mysql://localhost:3306/database?useUnicode=true&characterEncoding=UTF-8
     * (Required)
     * 
     */
    private String jdbcurl;
    private String dbusername = "root";
    private String dbpassword = "kedacom";
    private Long startoffset = 0L;
    /**
     * select * from tablename as t1 join (select id from tablename order by id limit ?, 1) as t2 where t1.id >= t2.id order by t1.id limit 500
     * (Required)
     * 
     */
    private String selectsql;
    private final static long serialVersionUID = 564354434681L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Jdbcsourcespec() {
    }

    /**
     * 
     * @param startoffset
     * @param parallel
     * @param dbpassword
     * @param jdbcurl
     * @param dbusername
     * @param selectsql
     */
    public Jdbcsourcespec(Long parallel, String jdbcurl, String dbusername, String dbpassword, Long startoffset, String selectsql) {
        super();
        this.parallel = parallel;
        this.jdbcurl = jdbcurl;
        this.dbusername = dbusername;
        this.dbpassword = dbpassword;
        this.startoffset = startoffset;
        this.selectsql = selectsql;
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
     * jdbc:mysql://localhost:3306/database?useUnicode=true&characterEncoding=UTF-8
     * (Required)
     * 
     */
    public String getJdbcurl() {
        return jdbcurl;
    }

    /**
     * jdbc:mysql://localhost:3306/database?useUnicode=true&characterEncoding=UTF-8
     * (Required)
     * 
     */
    public void setJdbcurl(String jdbcurl) {
        this.jdbcurl = jdbcurl;
    }

    public String getDbusername() {
        return dbusername;
    }

    public void setDbusername(String dbusername) {
        this.dbusername = dbusername;
    }

    public String getDbpassword() {
        return dbpassword;
    }

    public void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }

    public Long getStartoffset() {
        return startoffset;
    }

    public void setStartoffset(Long startoffset) {
        this.startoffset = startoffset;
    }

    /**
     * select * from tablename as t1 join (select id from tablename order by id limit ?, 1) as t2 where t1.id >= t2.id order by t1.id limit 500
     * (Required)
     * 
     */
    public String getSelectsql() {
        return selectsql;
    }

    /**
     * select * from tablename as t1 join (select id from tablename order by id limit ?, 1) as t2 where t1.id >= t2.id order by t1.id limit 500
     * (Required)
     * 
     */
    public void setSelectsql(String selectsql) {
        this.selectsql = selectsql;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Jdbcsourcespec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("jdbcurl");
        sb.append('=');
        sb.append(((this.jdbcurl == null)?"<null>":this.jdbcurl));
        sb.append(',');
        sb.append("dbusername");
        sb.append('=');
        sb.append(((this.dbusername == null)?"<null>":this.dbusername));
        sb.append(',');
        sb.append("dbpassword");
        sb.append('=');
        sb.append(((this.dbpassword == null)?"<null>":this.dbpassword));
        sb.append(',');
        sb.append("startoffset");
        sb.append('=');
        sb.append(((this.startoffset == null)?"<null>":this.startoffset));
        sb.append(',');
        sb.append("selectsql");
        sb.append('=');
        sb.append(((this.selectsql == null)?"<null>":this.selectsql));
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
        result = ((result* 31)+((this.startoffset == null)? 0 :this.startoffset.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.dbpassword == null)? 0 :this.dbpassword.hashCode()));
        result = ((result* 31)+((this.jdbcurl == null)? 0 :this.jdbcurl.hashCode()));
        result = ((result* 31)+((this.dbusername == null)? 0 :this.dbusername.hashCode()));
        result = ((result* 31)+((this.selectsql == null)? 0 :this.selectsql.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Jdbcsourcespec) == false) {
            return false;
        }
        Jdbcsourcespec rhs = ((Jdbcsourcespec) other);
        return (((((((this.startoffset == rhs.startoffset)||((this.startoffset!= null)&&this.startoffset.equals(rhs.startoffset)))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.dbpassword == rhs.dbpassword)||((this.dbpassword!= null)&&this.dbpassword.equals(rhs.dbpassword))))&&((this.jdbcurl == rhs.jdbcurl)||((this.jdbcurl!= null)&&this.jdbcurl.equals(rhs.jdbcurl))))&&((this.dbusername == rhs.dbusername)||((this.dbusername!= null)&&this.dbusername.equals(rhs.dbusername))))&&((this.selectsql == rhs.selectsql)||((this.selectsql!= null)&&this.selectsql.equals(rhs.selectsql))));
    }

}
