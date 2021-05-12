
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Jdbcsinkspec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    /**
     * jdbc:mysql://localhost:3306/testdb?useUnicode=true&characterEncoding=UTF-8
     * (Required)
     * 
     */
    private String jdbcurl;
    private String dbusername = "root";
    private String dbpassword = "kedacom";
    /**
     * insert into testtable(testtime, testcomment) values (?, ?);
     * (Required)
     * 
     */
    private String insertsql;
    private Long batchnums = 1000L;
    private final static long serialVersionUID = 936595238045L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Jdbcsinkspec() {
    }

    /**
     * 
     * @param insertsql
     * @param batchnums
     * @param parallel
     * @param dbpassword
     * @param jdbcurl
     * @param dbusername
     */
    public Jdbcsinkspec(Long parallel, String jdbcurl, String dbusername, String dbpassword, String insertsql, Long batchnums) {
        super();
        this.parallel = parallel;
        this.jdbcurl = jdbcurl;
        this.dbusername = dbusername;
        this.dbpassword = dbpassword;
        this.insertsql = insertsql;
        this.batchnums = batchnums;
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
     * jdbc:mysql://localhost:3306/testdb?useUnicode=true&characterEncoding=UTF-8
     * (Required)
     * 
     */
    public String getJdbcurl() {
        return jdbcurl;
    }

    /**
     * jdbc:mysql://localhost:3306/testdb?useUnicode=true&characterEncoding=UTF-8
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
     * insert into testtable(testtime, testcomment) values (?, ?);
     * (Required)
     * 
     */
    public String getInsertsql() {
        return insertsql;
    }

    /**
     * insert into testtable(testtime, testcomment) values (?, ?);
     * (Required)
     * 
     */
    public void setInsertsql(String insertsql) {
        this.insertsql = insertsql;
    }

    public Long getBatchnums() {
        return batchnums;
    }

    public void setBatchnums(Long batchnums) {
        this.batchnums = batchnums;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Jdbcsinkspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        sb.append("insertsql");
        sb.append('=');
        sb.append(((this.insertsql == null)?"<null>":this.insertsql));
        sb.append(',');
        sb.append("batchnums");
        sb.append('=');
        sb.append(((this.batchnums == null)?"<null>":this.batchnums));
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
        result = ((result* 31)+((this.insertsql == null)? 0 :this.insertsql.hashCode()));
        result = ((result* 31)+((this.batchnums == null)? 0 :this.batchnums.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.dbpassword == null)? 0 :this.dbpassword.hashCode()));
        result = ((result* 31)+((this.jdbcurl == null)? 0 :this.jdbcurl.hashCode()));
        result = ((result* 31)+((this.dbusername == null)? 0 :this.dbusername.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Jdbcsinkspec) == false) {
            return false;
        }
        Jdbcsinkspec rhs = ((Jdbcsinkspec) other);
        return (((((((this.insertsql == rhs.insertsql)||((this.insertsql!= null)&&this.insertsql.equals(rhs.insertsql)))&&((this.batchnums == rhs.batchnums)||((this.batchnums!= null)&&this.batchnums.equals(rhs.batchnums))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.dbpassword == rhs.dbpassword)||((this.dbpassword!= null)&&this.dbpassword.equals(rhs.dbpassword))))&&((this.jdbcurl == rhs.jdbcurl)||((this.jdbcurl!= null)&&this.jdbcurl.equals(rhs.jdbcurl))))&&((this.dbusername == rhs.dbusername)||((this.dbusername!= null)&&this.dbusername.equals(rhs.dbusername))));
    }

}
