
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cepspec implements Serializable
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
    private String keyselector;
    /**
     * within, unit is milliseconds
     * 
     */
    private Long withintime = 0L;
    /**
     * AfterMatchSkipStrategy mode
     * 
     */
    private Cepspec.Skipstrategy skipstrategy;
    /**
     * pattern sequence defined use the my pattern seq ruler
     * (Required)
     * 
     */
    private String patternsequence;
    private List<Patternexp> patternexps = new ArrayList<Patternexp>();
    private final static long serialVersionUID = 730303414532L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Cepspec() {
    }

    /**
     * 
     * @param keyselector
     * @param skipstrategy
     * @param parallel
     * @param patternsequence
     * @param withintime
     * @param patternexps
     */
    public Cepspec(Long parallel, String keyselector, Long withintime, Cepspec.Skipstrategy skipstrategy, String patternsequence, List<Patternexp> patternexps) {
        super();
        this.parallel = parallel;
        this.keyselector = keyselector;
        this.withintime = withintime;
        this.skipstrategy = skipstrategy;
        this.patternsequence = patternsequence;
        this.patternexps = patternexps;
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
    public String getKeyselector() {
        return keyselector;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setKeyselector(String keyselector) {
        this.keyselector = keyselector;
    }

    /**
     * within, unit is milliseconds
     * 
     */
    public Long getWithintime() {
        return withintime;
    }

    /**
     * within, unit is milliseconds
     * 
     */
    public void setWithintime(Long withintime) {
        this.withintime = withintime;
    }

    /**
     * AfterMatchSkipStrategy mode
     * 
     */
    public Cepspec.Skipstrategy getSkipstrategy() {
        return skipstrategy;
    }

    /**
     * AfterMatchSkipStrategy mode
     * 
     */
    public void setSkipstrategy(Cepspec.Skipstrategy skipstrategy) {
        this.skipstrategy = skipstrategy;
    }

    /**
     * pattern sequence defined use the my pattern seq ruler
     * (Required)
     * 
     */
    public String getPatternsequence() {
        return patternsequence;
    }

    /**
     * pattern sequence defined use the my pattern seq ruler
     * (Required)
     * 
     */
    public void setPatternsequence(String patternsequence) {
        this.patternsequence = patternsequence;
    }

    public List<Patternexp> getPatternexps() {
        return patternexps;
    }

    public void setPatternexps(List<Patternexp> patternexps) {
        this.patternexps = patternexps;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Cepspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("keyselector");
        sb.append('=');
        sb.append(((this.keyselector == null)?"<null>":this.keyselector));
        sb.append(',');
        sb.append("withintime");
        sb.append('=');
        sb.append(((this.withintime == null)?"<null>":this.withintime));
        sb.append(',');
        sb.append("skipstrategy");
        sb.append('=');
        sb.append(((this.skipstrategy == null)?"<null>":this.skipstrategy));
        sb.append(',');
        sb.append("patternsequence");
        sb.append('=');
        sb.append(((this.patternsequence == null)?"<null>":this.patternsequence));
        sb.append(',');
        sb.append("patternexps");
        sb.append('=');
        sb.append(((this.patternexps == null)?"<null>":this.patternexps));
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
        result = ((result* 31)+((this.keyselector == null)? 0 :this.keyselector.hashCode()));
        result = ((result* 31)+((this.skipstrategy == null)? 0 :this.skipstrategy.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.patternsequence == null)? 0 :this.patternsequence.hashCode()));
        result = ((result* 31)+((this.withintime == null)? 0 :this.withintime.hashCode()));
        result = ((result* 31)+((this.patternexps == null)? 0 :this.patternexps.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Cepspec) == false) {
            return false;
        }
        Cepspec rhs = ((Cepspec) other);
        return (((((((this.keyselector == rhs.keyselector)||((this.keyselector!= null)&&this.keyselector.equals(rhs.keyselector)))&&((this.skipstrategy == rhs.skipstrategy)||((this.skipstrategy!= null)&&this.skipstrategy.equals(rhs.skipstrategy))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.patternsequence == rhs.patternsequence)||((this.patternsequence!= null)&&this.patternsequence.equals(rhs.patternsequence))))&&((this.withintime == rhs.withintime)||((this.withintime!= null)&&this.withintime.equals(rhs.withintime))))&&((this.patternexps == rhs.patternexps)||((this.patternexps!= null)&&this.patternexps.equals(rhs.patternexps))));
    }

    public enum Skipstrategy {

        NOSKIP("NOSKIP"),
        SKIPPASTLASTEVENT("SKIPPASTLASTEVENT"),
        SKIPTONEXT("SKIPTONEXT"),
        SKIPTOFIRST("SKIPTOFIRST"),
        SKIPTOLAST("SKIPTOLAST");
        private final String value;
        private final static Map<String, Cepspec.Skipstrategy> CONSTANTS = new HashMap<String, Cepspec.Skipstrategy>();

        static {
            for (Cepspec.Skipstrategy c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Skipstrategy(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Cepspec.Skipstrategy fromValue(String value) {
            Cepspec.Skipstrategy constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
