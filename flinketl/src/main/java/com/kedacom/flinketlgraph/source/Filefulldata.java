
package com.kedacom.flinketlgraph.source;

import java.io.Serializable;

public class Filefulldata implements Serializable
{
    private String filename;
    private Object content;
    private Long linenum;

    public Filefulldata() {
    }

    public Filefulldata(String filename, Object content, Long linenum) {
        this.filename = filename;
        this.content = content;
        this.linenum = linenum;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Long getLinenum() {
        return linenum;
    }

    public void setLinenum(Long linenum) {
        this.linenum = linenum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Filefulldata.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("filename");
        sb.append('=');
        sb.append(((this.filename == null)?"<null>":this.filename));
        sb.append(',');
        sb.append("content");
        sb.append('=');
        sb.append(((this.content == null)?"<null>":this.content));
        sb.append(',');
        sb.append("linenum");
        sb.append('=');
        sb.append(((this.linenum == null)?"<null>":this.linenum));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
