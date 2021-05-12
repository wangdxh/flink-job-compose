
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Redissourcespec implements Serializable
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
    private Redissinkspec redisinfo;
    /**
     * supported redis commands: get, hget, hgetall
     * 
     */
    private Redissourcespec.Command command;
    /**
     * required. this key stands for the primary KEY
     * (Required)
     * 
     */
    private String rediskey;
    /**
     * option
     * 
     */
    private String additionalkey;
    /**
     * the interval refresh the key value, seconds
     * 
     */
    private Long increment = 0L;
    private final static long serialVersionUID = 266862523189L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Redissourcespec() {
    }

    /**
     * 
     * @param parallel
     * @param additionalkey
     * @param rediskey
     * @param increment
     * @param redisinfo
     * @param command
     */
    public Redissourcespec(Long parallel, Redissinkspec redisinfo, Redissourcespec.Command command, String rediskey, String additionalkey, Long increment) {
        super();
        this.parallel = parallel;
        this.redisinfo = redisinfo;
        this.command = command;
        this.rediskey = rediskey;
        this.additionalkey = additionalkey;
        this.increment = increment;
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
    public Redissinkspec getRedisinfo() {
        return redisinfo;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setRedisinfo(Redissinkspec redisinfo) {
        this.redisinfo = redisinfo;
    }

    /**
     * supported redis commands: get, hget, hgetall
     * 
     */
    public Redissourcespec.Command getCommand() {
        return command;
    }

    /**
     * supported redis commands: get, hget, hgetall
     * 
     */
    public void setCommand(Redissourcespec.Command command) {
        this.command = command;
    }

    /**
     * required. this key stands for the primary KEY
     * (Required)
     * 
     */
    public String getRediskey() {
        return rediskey;
    }

    /**
     * required. this key stands for the primary KEY
     * (Required)
     * 
     */
    public void setRediskey(String rediskey) {
        this.rediskey = rediskey;
    }

    /**
     * option
     * 
     */
    public String getAdditionalkey() {
        return additionalkey;
    }

    /**
     * option
     * 
     */
    public void setAdditionalkey(String additionalkey) {
        this.additionalkey = additionalkey;
    }

    /**
     * the interval refresh the key value, seconds
     * 
     */
    public Long getIncrement() {
        return increment;
    }

    /**
     * the interval refresh the key value, seconds
     * 
     */
    public void setIncrement(Long increment) {
        this.increment = increment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Redissourcespec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("redisinfo");
        sb.append('=');
        sb.append(((this.redisinfo == null)?"<null>":this.redisinfo));
        sb.append(',');
        sb.append("command");
        sb.append('=');
        sb.append(((this.command == null)?"<null>":this.command));
        sb.append(',');
        sb.append("rediskey");
        sb.append('=');
        sb.append(((this.rediskey == null)?"<null>":this.rediskey));
        sb.append(',');
        sb.append("additionalkey");
        sb.append('=');
        sb.append(((this.additionalkey == null)?"<null>":this.additionalkey));
        sb.append(',');
        sb.append("increment");
        sb.append('=');
        sb.append(((this.increment == null)?"<null>":this.increment));
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
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.additionalkey == null)? 0 :this.additionalkey.hashCode()));
        result = ((result* 31)+((this.rediskey == null)? 0 :this.rediskey.hashCode()));
        result = ((result* 31)+((this.increment == null)? 0 :this.increment.hashCode()));
        result = ((result* 31)+((this.redisinfo == null)? 0 :this.redisinfo.hashCode()));
        result = ((result* 31)+((this.command == null)? 0 :this.command.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Redissourcespec) == false) {
            return false;
        }
        Redissourcespec rhs = ((Redissourcespec) other);
        return (((((((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel)))&&((this.additionalkey == rhs.additionalkey)||((this.additionalkey!= null)&&this.additionalkey.equals(rhs.additionalkey))))&&((this.rediskey == rhs.rediskey)||((this.rediskey!= null)&&this.rediskey.equals(rhs.rediskey))))&&((this.increment == rhs.increment)||((this.increment!= null)&&this.increment.equals(rhs.increment))))&&((this.redisinfo == rhs.redisinfo)||((this.redisinfo!= null)&&this.redisinfo.equals(rhs.redisinfo))))&&((this.command == rhs.command)||((this.command!= null)&&this.command.equals(rhs.command))));
    }

    public enum Command {

        GET("GET"),
        HGET("HGET"),
        HGETALL("HGETALL");
        private final String value;
        private final static Map<String, Redissourcespec.Command> CONSTANTS = new HashMap<String, Redissourcespec.Command>();

        static {
            for (Redissourcespec.Command c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Command(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Redissourcespec.Command fromValue(String value) {
            Redissourcespec.Command constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
