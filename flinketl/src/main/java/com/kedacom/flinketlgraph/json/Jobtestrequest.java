
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Jobtestrequest implements Serializable
{

    private Long timeout = 10L;
    private Long datanums = 10L;
    private Graph jobconfig;
    private String jsonfile;
    private final static long serialVersionUID = 291473409581L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Jobtestrequest() {
    }

    /**
     * 
     * @param jobconfig
     * @param jsonfile
     * @param datanums
     * @param timeout
     */
    public Jobtestrequest(Long timeout, Long datanums, Graph jobconfig, String jsonfile) {
        super();
        this.timeout = timeout;
        this.datanums = datanums;
        this.jobconfig = jobconfig;
        this.jsonfile = jsonfile;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Long getDatanums() {
        return datanums;
    }

    public void setDatanums(Long datanums) {
        this.datanums = datanums;
    }

    public Graph getJobconfig() {
        return jobconfig;
    }

    public void setJobconfig(Graph jobconfig) {
        this.jobconfig = jobconfig;
    }

    public String getJsonfile() {
        return jsonfile;
    }

    public void setJsonfile(String jsonfile) {
        this.jsonfile = jsonfile;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Jobtestrequest.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("timeout");
        sb.append('=');
        sb.append(((this.timeout == null)?"<null>":this.timeout));
        sb.append(',');
        sb.append("datanums");
        sb.append('=');
        sb.append(((this.datanums == null)?"<null>":this.datanums));
        sb.append(',');
        sb.append("jobconfig");
        sb.append('=');
        sb.append(((this.jobconfig == null)?"<null>":this.jobconfig));
        sb.append(',');
        sb.append("jsonfile");
        sb.append('=');
        sb.append(((this.jsonfile == null)?"<null>":this.jsonfile));
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
        result = ((result* 31)+((this.datanums == null)? 0 :this.datanums.hashCode()));
        result = ((result* 31)+((this.jobconfig == null)? 0 :this.jobconfig.hashCode()));
        result = ((result* 31)+((this.jsonfile == null)? 0 :this.jsonfile.hashCode()));
        result = ((result* 31)+((this.timeout == null)? 0 :this.timeout.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Jobtestrequest) == false) {
            return false;
        }
        Jobtestrequest rhs = ((Jobtestrequest) other);
        return (((((this.datanums == rhs.datanums)||((this.datanums!= null)&&this.datanums.equals(rhs.datanums)))&&((this.jobconfig == rhs.jobconfig)||((this.jobconfig!= null)&&this.jobconfig.equals(rhs.jobconfig))))&&((this.jsonfile == rhs.jsonfile)||((this.jsonfile!= null)&&this.jsonfile.equals(rhs.jsonfile))))&&((this.timeout == rhs.timeout)||((this.timeout!= null)&&this.timeout.equals(rhs.timeout))));
    }

}
