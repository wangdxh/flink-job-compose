
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;

public class Jobtestresult implements Serializable
{

    private Long code;
    private String message;
    private String exceptionstack;
    private Object data;
    private final static long serialVersionUID = 366400899817L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Jobtestresult() {
    }

    /**
     * 
     * @param exceptionstack
     * @param code
     * @param data
     * @param message
     */
    public Jobtestresult(Long code, String message, String exceptionstack, Object data) {
        super();
        this.code = code;
        this.message = message;
        this.exceptionstack = exceptionstack;
        this.data = data;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExceptionstack() {
        return exceptionstack;
    }

    public void setExceptionstack(String exceptionstack) {
        this.exceptionstack = exceptionstack;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Jobtestresult.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("code");
        sb.append('=');
        sb.append(((this.code == null)?"<null>":this.code));
        sb.append(',');
        sb.append("message");
        sb.append('=');
        sb.append(((this.message == null)?"<null>":this.message));
        sb.append(',');
        sb.append("exceptionstack");
        sb.append('=');
        sb.append(((this.exceptionstack == null)?"<null>":this.exceptionstack));
        sb.append(',');
        sb.append("data");
        sb.append('=');
        sb.append(((this.data == null)?"<null>":this.data));
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
        result = ((result* 31)+((this.exceptionstack == null)? 0 :this.exceptionstack.hashCode()));
        result = ((result* 31)+((this.code == null)? 0 :this.code.hashCode()));
        result = ((result* 31)+((this.message == null)? 0 :this.message.hashCode()));
        result = ((result* 31)+((this.data == null)? 0 :this.data.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Jobtestresult) == false) {
            return false;
        }
        Jobtestresult rhs = ((Jobtestresult) other);
        return (((((this.exceptionstack == rhs.exceptionstack)||((this.exceptionstack!= null)&&this.exceptionstack.equals(rhs.exceptionstack)))&&((this.code == rhs.code)||((this.code!= null)&&this.code.equals(rhs.code))))&&((this.message == rhs.message)||((this.message!= null)&&this.message.equals(rhs.message))))&&((this.data == rhs.data)||((this.data!= null)&&this.data.equals(rhs.data))));
    }

}
