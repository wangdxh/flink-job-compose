
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Redissinkspec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    /**
     *  127.0.0.1
     * (Required)
     * 
     */
    private String redishost;
    private Long redisport = 6379L;
    private Long dbindex = 0L;
    private String password = "";
    /**
     * now not support cluster mode
     * 
     */
    private String cluster;
    /**
     * the codec of value
     * 
     */
    private Redissinkspec.Valuecodec valuecodec = Redissinkspec.Valuecodec.fromValue("STRING");
    private final static long serialVersionUID = 777206752683L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Redissinkspec() {
    }

    /**
     * 
     * @param dbindex
     * @param cluster
     * @param redishost
     * @param password
     * @param parallel
     * @param valuecodec
     * @param redisport
     */
    public Redissinkspec(Long parallel, String redishost, Long redisport, Long dbindex, String password, String cluster, Redissinkspec.Valuecodec valuecodec) {
        super();
        this.parallel = parallel;
        this.redishost = redishost;
        this.redisport = redisport;
        this.dbindex = dbindex;
        this.password = password;
        this.cluster = cluster;
        this.valuecodec = valuecodec;
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
     *  127.0.0.1
     * (Required)
     * 
     */
    public String getRedishost() {
        return redishost;
    }

    /**
     *  127.0.0.1
     * (Required)
     * 
     */
    public void setRedishost(String redishost) {
        this.redishost = redishost;
    }

    public Long getRedisport() {
        return redisport;
    }

    public void setRedisport(Long redisport) {
        this.redisport = redisport;
    }

    public Long getDbindex() {
        return dbindex;
    }

    public void setDbindex(Long dbindex) {
        this.dbindex = dbindex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * now not support cluster mode
     * 
     */
    public String getCluster() {
        return cluster;
    }

    /**
     * now not support cluster mode
     * 
     */
    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    /**
     * the codec of value
     * 
     */
    public Redissinkspec.Valuecodec getValuecodec() {
        return valuecodec;
    }

    /**
     * the codec of value
     * 
     */
    public void setValuecodec(Redissinkspec.Valuecodec valuecodec) {
        this.valuecodec = valuecodec;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Redissinkspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("redishost");
        sb.append('=');
        sb.append(((this.redishost == null)?"<null>":this.redishost));
        sb.append(',');
        sb.append("redisport");
        sb.append('=');
        sb.append(((this.redisport == null)?"<null>":this.redisport));
        sb.append(',');
        sb.append("dbindex");
        sb.append('=');
        sb.append(((this.dbindex == null)?"<null>":this.dbindex));
        sb.append(',');
        sb.append("password");
        sb.append('=');
        sb.append(((this.password == null)?"<null>":this.password));
        sb.append(',');
        sb.append("cluster");
        sb.append('=');
        sb.append(((this.cluster == null)?"<null>":this.cluster));
        sb.append(',');
        sb.append("valuecodec");
        sb.append('=');
        sb.append(((this.valuecodec == null)?"<null>":this.valuecodec));
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
        result = ((result* 31)+((this.dbindex == null)? 0 :this.dbindex.hashCode()));
        result = ((result* 31)+((this.cluster == null)? 0 :this.cluster.hashCode()));
        result = ((result* 31)+((this.redishost == null)? 0 :this.redishost.hashCode()));
        result = ((result* 31)+((this.password == null)? 0 :this.password.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.valuecodec == null)? 0 :this.valuecodec.hashCode()));
        result = ((result* 31)+((this.redisport == null)? 0 :this.redisport.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Redissinkspec) == false) {
            return false;
        }
        Redissinkspec rhs = ((Redissinkspec) other);
        return ((((((((this.dbindex == rhs.dbindex)||((this.dbindex!= null)&&this.dbindex.equals(rhs.dbindex)))&&((this.cluster == rhs.cluster)||((this.cluster!= null)&&this.cluster.equals(rhs.cluster))))&&((this.redishost == rhs.redishost)||((this.redishost!= null)&&this.redishost.equals(rhs.redishost))))&&((this.password == rhs.password)||((this.password!= null)&&this.password.equals(rhs.password))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.valuecodec == rhs.valuecodec)||((this.valuecodec!= null)&&this.valuecodec.equals(rhs.valuecodec))))&&((this.redisport == rhs.redisport)||((this.redisport!= null)&&this.redisport.equals(rhs.redisport))));
    }

    public enum Valuecodec {

        STRING("STRING"),
        BYTEARRAY("BYTEARRAY");
        private final String value;
        private final static Map<String, Redissinkspec.Valuecodec> CONSTANTS = new HashMap<String, Redissinkspec.Valuecodec>();

        static {
            for (Redissinkspec.Valuecodec c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Valuecodec(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Redissinkspec.Valuecodec fromValue(String value) {
            Redissinkspec.Valuecodec constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
