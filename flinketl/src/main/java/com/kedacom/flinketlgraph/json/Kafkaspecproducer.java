
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Kafkaspecproducer implements Serializable
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
    private String topic;
    /**
     * 
     * (Required)
     * 
     */
    private String brokers;
    private final static long serialVersionUID = 792855805812L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Kafkaspecproducer() {
    }

    /**
     * 
     * @param brokers
     * @param parallel
     * @param topic
     */
    public Kafkaspecproducer(Long parallel, String topic, String brokers) {
        super();
        this.parallel = parallel;
        this.topic = topic;
        this.brokers = brokers;
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
    public String getTopic() {
        return topic;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setTopic(String topic) {
        this.topic = topic;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Kafkaspecproducer.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("topic");
        sb.append('=');
        sb.append(((this.topic == null)?"<null>":this.topic));
        sb.append(',');
        sb.append("brokers");
        sb.append('=');
        sb.append(((this.brokers == null)?"<null>":this.brokers));
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
        result = ((result* 31)+((this.topic == null)? 0 :this.topic.hashCode()));
        result = ((result* 31)+((this.brokers == null)? 0 :this.brokers.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Kafkaspecproducer) == false) {
            return false;
        }
        Kafkaspecproducer rhs = ((Kafkaspecproducer) other);
        return ((((this.topic == rhs.topic)||((this.topic!= null)&&this.topic.equals(rhs.topic)))&&((this.brokers == rhs.brokers)||((this.brokers!= null)&&this.brokers.equals(rhs.brokers))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))));
    }

}
