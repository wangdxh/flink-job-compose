package com.kedacom.flinketlgraph.transform;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 保存统计中的信息
 *  转存transformmetric中的信息进一步序列化
 */
public class TransmetricState implements Serializable {
    private static final long serialVersionUID = 1L;

    private long processedNum;
    private long failNum;
    private Map<String, Long> excepMap;

    public TransmetricState(){
        processedNum = 0L;
        failNum = 0;
        excepMap = new HashMap<>();
    }

    public TransmetricState(long processedNum, long failNum, Map<String, Long> excepMap) {
        this.processedNum = processedNum;
        this.failNum = failNum;
        this.excepMap = excepMap;
    }

    public void clear(){
        processedNum = 0L;
        failNum = 0;
        excepMap.clear();
    }

    public void AddTransmetricState(TransmetricState state){
        this.processedNum += state.processedNum;
        this.failNum += state.failNum;
        state.excepMap.forEach((k, v)->{
            this.excepMap.merge(k, v, Long::sum);
        });
    }

    public long getProcessedNum() {
        return processedNum;
    }

    public void setProcessedNum(long processedNum) {
        this.processedNum = processedNum;
    }

    public long getFailNum() {
        return failNum;
    }

    public void setFailNum(long failNum) {
        this.failNum = failNum;
    }

    public Map<String, Long> getExcepMap() {
        return excepMap;
    }

    public void setExcepMap(Map<String, Long> excepMap) {
        this.excepMap = excepMap;
    }

    @Override
    public String toString() {
        //return String.format("processedNum : %s, failNum : %s, excepmap : %s", processedNum, failNum, excepMap.toString());
        String strret = "TransmetricState can not seriealized";
        ObjectMapper mapper = new ObjectMapper();
        try {
            strret = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return strret;
    }

    public void processAddCount(long i){
        this.processedNum += i;
    }
    public void failAddCount(long i){
        this.failNum += i;
    }
    public void excepsAddCount(String excepMsg){
        this.excepMap.merge(excepMsg, 1L, Long::sum);
    }

    public void update(long failed, String exMsg){
        failAddCount(failed);
        if (StringUtils.isNotBlank(exMsg)){
            excepsAddCount(exMsg);
        }
    }
}
