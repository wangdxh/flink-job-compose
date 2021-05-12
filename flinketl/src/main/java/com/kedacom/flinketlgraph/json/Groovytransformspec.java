
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Groovytransformspec implements Serializable
{

    /**
     * 独立配置算子的并发度
     * 
     */
    private Long parallel = 0L;
    private String groovyfilepath;
    private String groovyfilecontent;
    /**
     * when u debug groovyfile, this is true, after debugging set this to false
     * 
     */
    private Boolean throwgroovyexception = false;
    private final static long serialVersionUID = 612480755538L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Groovytransformspec() {
    }

    /**
     * 
     * @param throwgroovyexception
     * @param groovyfilepath
     * @param parallel
     * @param groovyfilecontent
     */
    public Groovytransformspec(Long parallel, String groovyfilepath, String groovyfilecontent, Boolean throwgroovyexception) {
        super();
        this.parallel = parallel;
        this.groovyfilepath = groovyfilepath;
        this.groovyfilecontent = groovyfilecontent;
        this.throwgroovyexception = throwgroovyexception;
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

    public String getGroovyfilepath() {
        return groovyfilepath;
    }

    public void setGroovyfilepath(String groovyfilepath) {
        this.groovyfilepath = groovyfilepath;
    }

    public String getGroovyfilecontent() {
        return groovyfilecontent;
    }

    public void setGroovyfilecontent(String groovyfilecontent) {
        this.groovyfilecontent = groovyfilecontent;
    }

    /**
     * when u debug groovyfile, this is true, after debugging set this to false
     * 
     */
    public Boolean getThrowgroovyexception() {
        return throwgroovyexception;
    }

    /**
     * when u debug groovyfile, this is true, after debugging set this to false
     * 
     */
    public void setThrowgroovyexception(Boolean throwgroovyexception) {
        this.throwgroovyexception = throwgroovyexception;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Groovytransformspec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parallel");
        sb.append('=');
        sb.append(((this.parallel == null)?"<null>":this.parallel));
        sb.append(',');
        sb.append("groovyfilepath");
        sb.append('=');
        sb.append(((this.groovyfilepath == null)?"<null>":this.groovyfilepath));
        sb.append(',');
        sb.append("groovyfilecontent");
        sb.append('=');
        sb.append(((this.groovyfilecontent == null)?"<null>":this.groovyfilecontent));
        sb.append(',');
        sb.append("throwgroovyexception");
        sb.append('=');
        sb.append(((this.throwgroovyexception == null)?"<null>":this.throwgroovyexception));
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
        result = ((result* 31)+((this.throwgroovyexception == null)? 0 :this.throwgroovyexception.hashCode()));
        result = ((result* 31)+((this.groovyfilepath == null)? 0 :this.groovyfilepath.hashCode()));
        result = ((result* 31)+((this.parallel == null)? 0 :this.parallel.hashCode()));
        result = ((result* 31)+((this.groovyfilecontent == null)? 0 :this.groovyfilecontent.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Groovytransformspec) == false) {
            return false;
        }
        Groovytransformspec rhs = ((Groovytransformspec) other);
        return (((((this.throwgroovyexception == rhs.throwgroovyexception)||((this.throwgroovyexception!= null)&&this.throwgroovyexception.equals(rhs.throwgroovyexception)))&&((this.groovyfilepath == rhs.groovyfilepath)||((this.groovyfilepath!= null)&&this.groovyfilepath.equals(rhs.groovyfilepath))))&&((this.parallel == rhs.parallel)||((this.parallel!= null)&&this.parallel.equals(rhs.parallel))))&&((this.groovyfilecontent == rhs.groovyfilecontent)||((this.groovyfilecontent!= null)&&this.groovyfilecontent.equals(rhs.groovyfilecontent))));
    }

}
