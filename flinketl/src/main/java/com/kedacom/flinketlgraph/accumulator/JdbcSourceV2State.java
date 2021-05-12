package com.kedacom.flinketlgraph.accumulator;

import java.io.Serializable;

public class JdbcSourceV2State implements Serializable {
    private final static long serialVersionUID = 1L;

    private long totalnums;
    private long processedNum;
    private Object uniquecolumn;

    public JdbcSourceV2State(Long totalnums, Long processedNum, Object uniquecolumn) {
        this.totalnums = totalnums;
        this.processedNum = processedNum;
        this.uniquecolumn = uniquecolumn;
    }

    public JdbcSourceV2State() {
    }

    @Override
    public String toString() {
        return "{ \n" +
                "\"totalnums\":" + totalnums + ",\n" +
                "\"processedNum\":" + processedNum + ",\n" +
                "\"uniquecolumn\":" + uniquecolumn +
                "\n}";
    }
    public void clear() {
        this.totalnums = 0L;
        this.processedNum = 0L;
        this.uniquecolumn = null;
    }
    public Long getTotalnums() {
        return totalnums;
    }

    public void addProcessedNum(){
        this.processedNum += 1;
    }

    public void setTotalnums(Long totalnums) {
        this.totalnums = totalnums;
    }

    public Long getProcessedNum() {
        return processedNum;
    }

    public void setProcessedNum(Long processednums) {
        this.processedNum = processednums;
    }

    public Object getUniquecolumn() {
        return uniquecolumn;
    }

    public void setUniquecolumn(Object uniquecolumn) {
        this.uniquecolumn = uniquecolumn;
    }
}
