package com.kedacom.flinketlgraph.transform;

import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.util.Collector;

public class ConnectCoFlatMap implements CoFlatMapFunction<Object, Object, Object> {
    @Override
    public void flatMap1(Object value, Collector<Object> out) throws Exception {
        out.collect(value);
    }

    @Override
    public void flatMap2(Object value, Collector<Object> out) throws Exception {
        out.collect(value);
    }
}
