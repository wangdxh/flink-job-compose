
package com.kedacom.flinksql.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * sqlconfig配置
 * 
 */
public class Sqlconfig implements Serializable
{

    /**
     * flink job的并行度
     * (Required)
     * 
     */
    private Long parallel;
    /**
     * job的名称
     * (Required)
     * 
     */
    private String jobname;
    /**
     * job重启次数
     * 
     */
    private Long attempts = 2147483647L;
    /**
     * checkpoint的配置
     * (Required)
     * 
     */
    private Chkpointcfg chkpointcfg;
    /**
     * jar包路径
     * 
     */
    private List<String> dependjarpaths = new ArrayList<String>();
    private final static long serialVersionUID = -5932051432720750313L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Sqlconfig() {
    }

    /**
     * 
     * @param chkpointcfg
     * @param parallel
     * @param dependjarpaths
     * @param jobname
     * @param attempts
     */
    public Sqlconfig(Long parallel, String jobname, Long attempts, Chkpointcfg chkpointcfg, List<String> dependjarpaths) {
        super();
        this.parallel = parallel;
        this.jobname = jobname;
        this.attempts = attempts;
        this.chkpointcfg = chkpointcfg;
        this.dependjarpaths = dependjarpaths;
    }

    /**
     * flink job的并行度
     * (Required)
     * 
     */
    public Long getParallel() {
        return parallel;
    }

    /**
     * flink job的并行度
     * (Required)
     * 
     */
    public void setParallel(Long parallel) {
        this.parallel = parallel;
    }

    /**
     * job的名称
     * (Required)
     * 
     */
    public String getJobname() {
        return jobname;
    }

    /**
     * job的名称
     * (Required)
     * 
     */
    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    /**
     * job重启次数
     * 
     */
    public Long getAttempts() {
        return attempts;
    }

    /**
     * job重启次数
     * 
     */
    public void setAttempts(Long attempts) {
        this.attempts = attempts;
    }

    /**
     * checkpoint的配置
     * (Required)
     * 
     */
    public Chkpointcfg getChkpointcfg() {
        return chkpointcfg;
    }

    /**
     * checkpoint的配置
     * (Required)
     * 
     */
    public void setChkpointcfg(Chkpointcfg chkpointcfg) {
        this.chkpointcfg = chkpointcfg;
    }

    /**
     * jar包路径
     * 
     */
    public List<String> getDependjarpaths() {
        return dependjarpaths;
    }

    /**
     * jar包路径
     * 
     */
    public void setDependjarpaths(List<String> dependjarpaths) {
        this.dependjarpaths = dependjarpaths;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Sqlconfig.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("jobname");
        sb.append('=');
        sb.append(((this.jobname == null)?"<null>":this.jobname));
        sb.append(',');
        sb.append("attempts");
        sb.append('=');
        sb.append(((this.attempts == null)?"<null>":this.attempts));
        sb.append(',');
        sb.append("chkpointcfg");
        sb.append('=');
        sb.append(((this.chkpointcfg == null)?"<null>":this.chkpointcfg));
        sb.append(',');
        sb.append("dependjarpaths");
        sb.append('=');
        sb.append(((this.dependjarpaths == null)?"<null>":this.dependjarpaths));
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
        result = ((result* 31)+((this.chkpointcfg == null)? 0 :this.chkpointcfg.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.dependjarpaths == null)? 0 :this.dependjarpaths.hashCode()));
        result = ((result* 31)+((this.jobname == null)? 0 :this.jobname.hashCode()));
        result = ((result* 31)+((this.attempts == null)? 0 :this.attempts.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Sqlconfig) == false) {
            return false;
        }
        Sqlconfig rhs = ((Sqlconfig) other);
        return ((((((this.chkpointcfg == rhs.chkpointcfg)||((this.chkpointcfg!= null)&&this.chkpointcfg.equals(rhs.chkpointcfg)))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.dependjarpaths == rhs.dependjarpaths)||((this.dependjarpaths!= null)&&this.dependjarpaths.equals(rhs.dependjarpaths))))&&((this.jobname == rhs.jobname)||((this.jobname!= null)&&this.jobname.equals(rhs.jobname))))&&((this.attempts == rhs.attempts)||((this.attempts!= null)&&this.attempts.equals(rhs.attempts))));
    }

}
