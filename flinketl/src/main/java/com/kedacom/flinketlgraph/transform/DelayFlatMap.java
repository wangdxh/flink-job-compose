package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.json.Delaytransformspec;
import com.kedacom.flinketlgraph.json.Graphnode;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

public class DelayFlatMap extends RichFlatMapFunction<Object, Object>{

    private static final long serialVersionUID = 4744381842859501540L;
    private Delaytransformspec spec;
    private transient boolean bsleeped = false;

    public DelayFlatMap(Graphnode trans) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        this.spec = mapper.convertValue(trans.getElementconfig(), Delaytransformspec.class);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void flatMap(Object value, Collector<Object> out) throws Exception {
        if (false == bsleeped){
            TimeUnit.SECONDS.sleep(spec.getDelayedtime());
            bsleeped = true;
        }
        out.collect(value);
    }

}
