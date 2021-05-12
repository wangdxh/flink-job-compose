
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Kafkaspecconsumer implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    /**
     * 
     * (Required)
     * 
     */
    private String topics;
    /**
     * 
     * (Required)
     * 
     */
    private String brokers;
    /**
     * 
     * (Required)
     * 
     */
    private String groupid;
    private Kafkaspecconsumer.Startfrom startfrom = Kafkaspecconsumer.Startfrom.fromValue("GROUPOFFSET");
    private final static long serialVersionUID = 43299635278L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Kafkaspecconsumer() {
    }

    /**
     * 
     * @param startfrom
     * @param brokers
     * @param parallel
     * @param topics
     * @param groupid
     */
    public Kafkaspecconsumer(Long parallel, String topics, String brokers, String groupid, Kafkaspecconsumer.Startfrom startfrom) {
        super();
        this.parallel = parallel;
        this.topics = topics;
        this.brokers = brokers;
        this.groupid = groupid;
        this.startfrom = startfrom;
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
     * 
     * (Required)
     * 
     */
    public String getTopics() {
        return topics;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setTopics(String topics) {
        this.topics = topics;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getBrokers() {
        return brokers;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setBrokers(String brokers) {
        this.brokers = brokers;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getGroupid() {
        return groupid;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public Kafkaspecconsumer.Startfrom getStartfrom() {
        return startfrom;
    }

    public void setStartfrom(Kafkaspecconsumer.Startfrom startfrom) {
        this.startfrom = startfrom;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Kafkaspecconsumer.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("topics");
        sb.append('=');
        sb.append(((this.topics == null)?"<null>":this.topics));
        sb.append(',');
        sb.append("brokers");
        sb.append('=');
        sb.append(((this.brokers == null)?"<null>":this.brokers));
        sb.append(',');
        sb.append("groupid");
        sb.append('=');
        sb.append(((this.groupid == null)?"<null>":this.groupid));
        sb.append(',');
        sb.append("startfrom");
        sb.append('=');
        sb.append(((this.startfrom == null)?"<null>":this.startfrom));
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
        result = ((result* 31)+((this.startfrom == null)? 0 :this.startfrom.hashCode()));
        result = ((result* 31)+((this.brokers == null)? 0 :this.brokers.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.topics == null)? 0 :this.topics.hashCode()));
        result = ((result* 31)+((this.groupid == null)? 0 :this.groupid.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Kafkaspecconsumer) == false) {
            return false;
        }
        Kafkaspecconsumer rhs = ((Kafkaspecconsumer) other);
        return ((((((this.startfrom == rhs.startfrom)||((this.startfrom!= null)&&this.startfrom.equals(rhs.startfrom)))&&((this.brokers == rhs.brokers)||((this.brokers!= null)&&this.brokers.equals(rhs.brokers))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.topics == rhs.topics)||((this.topics!= null)&&this.topics.equals(rhs.topics))))&&((this.groupid == rhs.groupid)||((this.groupid!= null)&&this.groupid.equals(rhs.groupid))));
    }

    public enum Startfrom {

        GROUPOFFSET("GROUPOFFSET"),
        EARLIEST("EARLIEST"),
        LATEST("LATEST");
        private final String value;
        private final static Map<String, Kafkaspecconsumer.Startfrom> CONSTANTS = new HashMap<String, Kafkaspecconsumer.Startfrom>();

        static {
            for (Kafkaspecconsumer.Startfrom c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Startfrom(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Kafkaspecconsumer.Startfrom fromValue(String value) {
            Kafkaspecconsumer.Startfrom constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
