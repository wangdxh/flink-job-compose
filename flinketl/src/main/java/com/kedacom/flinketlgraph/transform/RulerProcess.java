package com.kedacom.flinketlgraph.transform;

import org.apache.flink.api.common.functions.InvalidTypesException;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Map;

public class RulerProcess extends BroadcastProcessFunction<Object, Object, Object> {
    private MapStateDescriptor<String, String> desc;

    RulerProcess(MapStateDescriptor<String, String> desc) throws Exception {
        this.desc = desc;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void processElement(Object value, ReadOnlyContext ctx, Collector<Object> out) throws Exception {
        if (!(value instanceof Map)) {
            throw new InvalidTypesException("groovyflatmap type is not mapobject");
        }
        out.collect(value);
    }

    @Override
    public void processBroadcastElement(Object value, Context ctx, Collector<Object> out) throws Exception {

        if (!(value instanceof Map)) {
            throw new InvalidTypesException("groovyflatmap broadcast type is not mapobject");
        }
        Map o = (Map)value;
        o.put("_flinkisruler", true);
        out.collect(o);
    }

}
