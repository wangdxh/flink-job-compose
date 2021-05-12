package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.*;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Arrays;
import java.util.Map;

public class WatermarkerAndTimestampOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinktransform_watermarkandtimestamp",
            "watermarkandtimestamp transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/watermarkerandtimestampspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.mapobject),
                            "input 0",
                            "dataoinput"
                    )
            ),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.object),
                            "output 0",
                            "dataoutput"
                    )
            )
    );

    @Override
    public ElementDescriptor getoperatordesc()
    {
        return desc;
    }

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(DataStream<Object> trans, Graphnode transconfig, Graph graph) throws Exception{
        StreamExecutionEnvironment env = trans.getExecutionEnvironment();

        ObjectMapper mapper = new ObjectMapper();
        Watermarkerandtimestampspec spec = mapper.convertValue(transconfig.getElementconfig(), Watermarkerandtimestampspec.class);

        if (spec.getTimetype() == Watermarkerandtimestampspec.Timetype.EVENTTIME){
            env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

            if (spec.getWatermarkinterval() > 0){
                env.getConfig().setAutoWatermarkInterval(spec.getWatermarkinterval());
            }
        }

        SingleOutputStreamOperator<Object> out = null;

        if (spec.getBoundedoutoforderness() != null){
            AssignerWithPeriodicWatermarks<Object> assigner =
                    new BoundedOutOfOrdernessTimestampExtractor<Object>(
                            Time.milliseconds(spec.getBoundedoutoforderness().getMaxoutoforderness())) {
                @Override
                public long extractTimestamp(Object element) {
                    if (!(element instanceof Map)){
                        throw new RuntimeException("WatermarkerAndTimestampOperator input is not map object");
                    }

                    Map<String, Object> aaa = (Map<String, Object>)element;
                    long ret = (long)aaa.get(spec.getBoundedoutoforderness().getExtracttimestamp());
                    //System.out.println(ret);
                    return ret;
                }
            };
            out = trans.assignTimestampsAndWatermarks(assigner);
        }
        return out;
    }
}
