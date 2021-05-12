package com.kedacom.flinketlgraph.transform;

import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Map;

public class CepPatternProcessFunction extends PatternProcessFunction<Object, Object> {

    @Override
    public void processMatch(Map<String, List<Object>> map, Context context, Collector<Object> collector) throws Exception {
        collector.collect(map);
    }

}