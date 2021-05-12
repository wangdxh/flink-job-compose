
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Patternexp implements Serializable
{

    /**
     * pattern name
     * (Required)
     * 
     */
    private String name;
    /**
     * pattern aviator express
     * (Required)
     * 
     */
    private String aviatorexpress;
    private final static long serialVersionUID = 260853923241L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Patternexp() {
    }

    /**
     * 
     * @param aviatorexpress
     * @param name
     */
    public Patternexp(String name, String aviatorexpress) {
        super();
        this.name = name;
        this.aviatorexpress = aviatorexpress;
    }

    /**
     * pattern name
     * (Required)
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * pattern name
     * (Required)
     * 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * pattern aviator express
     * (Required)
     * 
     */
    public String getAviatorexpress() {
        return aviatorexpress;
    }

    /**
     * pattern aviator express
     * (Required)
     * 
     */
    public void setAviatorexpress(String aviatorexpress) {
        this.aviatorexpress = aviatorexpress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Patternexp.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("aviatorexpress");
        sb.append('=');
        sb.append(((this.aviatorexpress == null)?"<null>":this.aviatorexpress));
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
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.aviatorexpress == null)? 0 :this.aviatorexpress.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Patternexp) == false) {
            return false;
        }
        Patternexp rhs = ((Patternexp) other);
        return (((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.aviatorexpress == rhs.aviatorexpress)||((this.aviatorexpress!= null)&&this.aviatorexpress.equals(rhs.aviatorexpress))));
    }

}
