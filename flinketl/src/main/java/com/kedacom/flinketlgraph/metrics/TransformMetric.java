package com.kedacom.flinketlgraph.metrics;

import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Gauge;

import java.util.HashMap;
import java.util.Map;

/**
 * jsontransform csvtransfom 统计信息
 */
public class TransformMetric {

    private transient Counter processedconter;
    private transient Counter failconter;
    private transient Gauge<Map<String, Long>> excepsgauge;
    private transient Map<String, Long> excepsmap;

    public TransformMetric(RuntimeContext ctx, String strprocessedcounter, String strfailcounter, String strexcepguage) {
        if (StringUtils.isNotBlank(strprocessedcounter)){
            processedconter = ctx.getMetricGroup().counter(strprocessedcounter);
        }

        if(StringUtils.isNotBlank(strfailcounter)){
            failconter = ctx.getMetricGroup().counter(strfailcounter);
        }

        if (StringUtils.isNotBlank(strexcepguage)){
            excepsmap = new HashMap<>();
            excepsgauge = ctx.getMetricGroup()
                    .gauge(strexcepguage, new Gauge<Map<String, Long>>() {
                        @Override
                        public Map<String, Long> getValue() {
                            return excepsmap;
                        }
                    });
        }
    }

    public void processAddCount(long i){
        this.processedconter.inc(i);
    }
    public void failAddCount(long i){
        this.failconter.inc(i);
    }
    // 需要好好测试
    public void setExcepsmap(Map<String, Long> map){
        this.excepsmap = map;
    }
    public void excepsAddCount(String excepMsg, Object obj){
        excepsmap.merge(excepMsg, 1L, Long::sum);
    }

    public Long getProcessedNum(){
        return this.processedconter==null ? 0L : this.processedconter.getCount();
    }
    public Long getFailNum(){
        return this.failconter==null ? 0L : this.failconter.getCount();
    }
    public Map<String, Long> getExecpMap(){
        if (excepsgauge==null){
            return null;
        }else {
            return excepsmap;
        }
    }

}
