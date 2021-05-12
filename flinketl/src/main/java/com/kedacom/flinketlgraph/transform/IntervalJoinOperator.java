package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.*;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IntervalJoinOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinktransform_intervaljoin",
            "intervaljoin transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/intervaljoinspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.mapobject),
                            "left input",
                            "base compare data stream"
                    ),
                    new Elesingleportinfo(
                            1L,
                            Arrays.asList(CommonUtils.mapobject),
                            "right input",
                            "be compared data stream"
                    )
            ),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.joinresult),
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
    public SingleOutputStreamOperator<Object> CreateTransform(List<DataStream<Object>> translist, Graphnode transconfig, Graph graph) throws Exception{
        DataStream<Object> trans = translist.get(0);
        DataStream<Object> rightstream = translist.get(1);

        StreamExecutionEnvironment env = trans.getExecutionEnvironment();

        ObjectMapper mapper = new ObjectMapper();
        Intervaljoinspec spec = mapper.convertValue(transconfig.getElementconfig(), Intervaljoinspec.class);

        return trans.keyBy(new MyKeySelector(spec.getLeftkeyselector()))
                .intervalJoin(rightstream.keyBy(new MyKeySelector(spec.getRightkeyselector())))
                .between(Time.milliseconds(spec.getLowerbound()), Time.milliseconds(spec.getUpperbound()))
                .upperBoundExclusive()
                .process(new ProcessJoinFunction<Object, Object, Object>() {
                    @Override
                    public void processElement(Object left, Object right, Context ctx, Collector<Object> out) throws Exception {
                        if (!(left instanceof Map) || !(right instanceof Map)){
                            throw new Exception("when interval join two stream must be map object");
                        }
                        out.collect(new JoinResult((Map<String, Object>)left, (Map<String, Object>)right));
                    }
                });
    }
}
