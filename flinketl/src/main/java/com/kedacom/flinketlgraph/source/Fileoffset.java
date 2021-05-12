
package com.kedacom.flinketlgraph.source;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class Fileoffset implements Serializable, Cloneable, Comparable<Fileoffset>
{
    private static Logger logger = LoggerFactory.getLogger(Fileoffset.class);
    private final static long serialVersionUID = 4826465214775467054L;
    /**
     * file absolute path
     * 
     */
    private String filename;
    /**
     * offset, filepointer, the position that start to be read
     * 
     */
    private Long fileoffset;
    /**
     * line number processed
     */
    private Long processedNum;
    /**
     * file size , file.available()
     */
    private Long filesize;
    /**
     * this flag indicate whether completed processing this file. true : yes
     * 
     */
    private Boolean isover;
    /**
     * store the error message encountered when 
     * 
     */
    private String errormsg;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Fileoffset() {
    }

    /**
     */
    public Fileoffset(String filename, Long filesize, Long processedNum, Long fileoffset, Boolean isover, String errormsg) {
        super();
        this.filename = filename;
        this.filesize = filesize;
        this.processedNum = processedNum;
        this.fileoffset = fileoffset;
        this.isover = isover;
        this.errormsg = errormsg;
    }

    /**
     * file absolute path
     * 
     */
    public String getFilename() {
        return filename;
    }

    /**
     * file absolute path
     * 
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * offset, if normal textfile, offset stands for line number, if bytes block files, it stands for number of bytes read.
     * 
     */
    public Long getFileoffset() {
        return fileoffset;
    }

    /**
     * offset, if normal textfile, offset stands for line number, if bytes block files, it stands for number of bytes read.
     * 
     */
    public void setFileoffset(Long offset) {
        this.fileoffset= offset;
    }

    /**
     * this flag indicate whether completed processing this file. true : yes
     * 
     */
    public Boolean getIsover() {
        return isover;
    }

    /**
     * this flag indicate whether completed processing this file. true : yes
     * 
     */
    public void setIsover(Boolean isover) {
        this.isover = isover;
    }

    /**
     * store the error message encountered when 
     * 
     */
    public String getErrormsg() {
        return errormsg;
    }

    /**
     * store the error message encountered when 
     * 
     */
    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public Long getProcessedNum() {
        return processedNum;
    }

    public void setProcessedNum(Long linenum) {
        this.processedNum = linenum;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = "no data";
        try {
            jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.error("{}", e.getMessage());
        }
        return jsonStr;
    }

    @Override
    public int hashCode() {
        return this.filename.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Fileoffset) == false) {
            return false;
        }
        Fileoffset rhs = ((Fileoffset) other);
        return this.filename.equals(rhs.getFilename());
    }

    @Override
    public int compareTo(Fileoffset o) {
        return this.filename.compareTo(o.getFilename());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Fileoffset(this.getFilename(), this.getFilesize(), this.processedNum, this.getFileoffset(),this.getIsover(), this.getErrormsg());
    }
}
