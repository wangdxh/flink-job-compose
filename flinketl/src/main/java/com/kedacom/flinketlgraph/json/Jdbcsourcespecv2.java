
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Jdbcsourcespecv2 implements Serializable
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
    /**
     * 
     * (Required)
     * 
     */
    private String uniquecolumn;
    /**
     * select * from tablename where (a > b) and uniquecolumn > ? order by uniquecolumn asc limit 500
     * (Required)
     * 
     */
    private String selectsql;
    /**
     * select * from tablename where (a > b) order by uniquecolumn asc limit 500
     * (Required)
     * 
     */
    private String firstselectsql;
    /**
     * select count(*) as totalnum from tablename where (a>b)
     * (Required)
     * 
     */
    private String countsql;
    /**
     * is incremental or not
     * 
     */
    private Boolean incremental = false;
    private final static long serialVersionUID = 432331138211L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Jdbcsourcespecv2() {
    }

    /**
     * 
     * @param uniquecolumn
     * @param firstselectsql
     * @param parallel
     * @param dbpassword
     * @param jdbcurl
     * @param dbusername
     * @param selectsql
     * @param incremental
     * @param countsql
     */
    public Jdbcsourcespecv2(Long parallel, String jdbcurl, String dbusername, String dbpassword, String uniquecolumn, String selectsql, String firstselectsql, String countsql, Boolean incremental) {
        super();
        this.parallel = parallel;
        this.jdbcurl = jdbcurl;
        this.dbusername = dbusername;
        this.dbpassword = dbpassword;
        this.uniquecolumn = uniquecolumn;
        this.selectsql = selectsql;
        this.firstselectsql = firstselectsql;
        this.countsql = countsql;
        this.incremental = incremental;
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

    /**
     * 
     * (Required)
     * 
     */
    public String getUniquecolumn() {
        return uniquecolumn;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setUniquecolumn(String uniquecolumn) {
        this.uniquecolumn = uniquecolumn;
    }

    /**
     * select * from tablename where (a > b) and uniquecolumn > ? order by uniquecolumn asc limit 500
     * (Required)
     * 
     */
    public String getSelectsql() {
        return selectsql;
    }

    /**
     * select * from tablename where (a > b) and uniquecolumn > ? order by uniquecolumn asc limit 500
     * (Required)
     * 
     */
    public void setSelectsql(String selectsql) {
        this.selectsql = selectsql;
    }

    /**
     * select * from tablename where (a > b) order by uniquecolumn asc limit 500
     * (Required)
     * 
     */
    public String getFirstselectsql() {
        return firstselectsql;
    }

    /**
     * select * from tablename where (a > b) order by uniquecolumn asc limit 500
     * (Required)
     * 
     */
    public void setFirstselectsql(String firstselectsql) {
        this.firstselectsql = firstselectsql;
    }

    /**
     * select count(*) as totalnum from tablename where (a>b)
     * (Required)
     * 
     */
    public String getCountsql() {
        return countsql;
    }

    /**
     * select count(*) as totalnum from tablename where (a>b)
     * (Required)
     * 
     */
    public void setCountsql(String countsql) {
        this.countsql = countsql;
    }

    /**
     * is incremental or not
     * 
     */
    public Boolean getIncremental() {
        return incremental;
    }

    /**
     * is incremental or not
     * 
     */
    public void setIncremental(Boolean incremental) {
        this.incremental = incremental;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Jdbcsourcespecv2 .class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        sb.append("uniquecolumn");
        sb.append('=');
        sb.append(((this.uniquecolumn == null)?"<null>":this.uniquecolumn));
        sb.append(',');
        sb.append("selectsql");
        sb.append('=');
        sb.append(((this.selectsql == null)?"<null>":this.selectsql));
        sb.append(',');
        sb.append("firstselectsql");
        sb.append('=');
        sb.append(((this.firstselectsql == null)?"<null>":this.firstselectsql));
        sb.append(',');
        sb.append("countsql");
        sb.append('=');
        sb.append(((this.countsql == null)?"<null>":this.countsql));
        sb.append(',');
        sb.append("incremental");
        sb.append('=');
        sb.append(((this.incremental == null)?"<null>":this.incremental));
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
        result = ((result* 31)+((this.uniquecolumn == null)? 0 :this.uniquecolumn.hashCode()));
        result = ((result* 31)+((this.firstselectsql == null)? 0 :this.firstselectsql.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.dbpassword == null)? 0 :this.dbpassword.hashCode()));
        result = ((result* 31)+((this.jdbcurl == null)? 0 :this.jdbcurl.hashCode()));
        result = ((result* 31)+((this.dbusername == null)? 0 :this.dbusername.hashCode()));
        result = ((result* 31)+((this.selectsql == null)? 0 :this.selectsql.hashCode()));
        result = ((result* 31)+((this.incremental == null)? 0 :this.incremental.hashCode()));
        result = ((result* 31)+((this.countsql == null)? 0 :this.countsql.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Jdbcsourcespecv2) == false) {
            return false;
        }
        Jdbcsourcespecv2 rhs = ((Jdbcsourcespecv2) other);
        return ((((((((((this.uniquecolumn == rhs.uniquecolumn)||((this.uniquecolumn!= null)&&this.uniquecolumn.equals(rhs.uniquecolumn)))&&((this.firstselectsql == rhs.firstselectsql)||((this.firstselectsql!= null)&&this.firstselectsql.equals(rhs.firstselectsql))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.dbpassword == rhs.dbpassword)||((this.dbpassword!= null)&&this.dbpassword.equals(rhs.dbpassword))))&&((this.jdbcurl == rhs.jdbcurl)||((this.jdbcurl!= null)&&this.jdbcurl.equals(rhs.jdbcurl))))&&((this.dbusername == rhs.dbusername)||((this.dbusername!= null)&&this.dbusername.equals(rhs.dbusername))))&&((this.selectsql == rhs.selectsql)||((this.selectsql!= null)&&this.selectsql.equals(rhs.selectsql))))&&((this.incremental == rhs.incremental)||((this.incremental!= null)&&this.incremental.equals(rhs.incremental))))&&((this.countsql == rhs.countsql)||((this.countsql!= null)&&this.countsql.equals(rhs.countsql))));
    }

}
