package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.*;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.*;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JoinOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinktransform_join",
            "join transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/joinspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.mapobject),
                            "input 0",
                            "dataoinput"
                    ),
                    new Elesingleportinfo(
                            1L,
                            Arrays.asList(CommonUtils.mapobject),
                            "input 1",
                            "dataoinput"
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

        ObjectMapper mapper = new ObjectMapper();
        Joinspec spec = mapper.convertValue(transconfig.getElementconfig(), Joinspec.class);

        Windowinfo window = spec.getWindowinfo();

        //create rulerstream and connect to datastream  this is the key step
        DataStream<Object> rightstream = translist.get(1);
        StreamExecutionEnvironment env = trans.getExecutionEnvironment();

        WindowAssigner<Object, TimeWindow> windowassigner = null;
        if (window.getWindowtype() == Windowinfo.Windowtype.SLIDING){
            if (env.getStreamTimeCharacteristic() == TimeCharacteristic.EventTime){
                if (window.getOffset() > 0){
                    windowassigner = SlidingEventTimeWindows.of(Time.milliseconds(window.getSize()),
                            Time.milliseconds(window.getSlide()), Time.milliseconds(window.getOffset()));
                }else{
                    windowassigner = SlidingEventTimeWindows.of(Time.milliseconds(window.getSize()),
                            Time.milliseconds(window.getSlide()));
                }
            }else{
                if (window.getOffset() > 0){
                    windowassigner = SlidingProcessingTimeWindows.of(Time.milliseconds(window.getSize()),
                            Time.milliseconds(window.getSlide()), Time.milliseconds(window.getOffset()));
                }else{
                    windowassigner = SlidingProcessingTimeWindows.of(Time.milliseconds(window.getSize()),
                            Time.milliseconds(window.getSlide()));
                }
            }
        }else{
            if (env.getStreamTimeCharacteristic() == TimeCharacteristic.EventTime){
                if (window.getOffset() > 0){
                    windowassigner = TumblingEventTimeWindows.of(Time.milliseconds(window.getSize()),
                            Time.milliseconds(window.getOffset()));
                }else{
                    windowassigner = TumblingEventTimeWindows.of(Time.milliseconds(window.getSize()));
                }
            }else{
                if (window.getOffset() > 0){
                    windowassigner = TumblingProcessingTimeWindows.of(Time.milliseconds(window.getSize()),
                            Time.milliseconds(window.getOffset()));
                }else{
                    windowassigner = TumblingProcessingTimeWindows.of(Time.milliseconds(window.getSize()));
                }
            }
        }

        SingleOutputStreamOperator<Object> out =
                (SingleOutputStreamOperator<Object>)trans.join(rightstream)
                .where(new MyKeySelector(spec.getLeftkeyselector()))
                .equalTo(new MyKeySelector(spec.getRightkeyselector()))
                .window(windowassigner)
                .apply(new JoinFunction<Object, Object, Object>() {
                    @Override
                    public Object join(Object in1, Object in2) throws Exception {
                        if (!(in1 instanceof Map) || !(in2 instanceof Map)){
                            throw new Exception("when join two stream must be map object");
                        }
                        return new JoinResult((Map<String, Object>)in1, (Map<String, Object>)in2);
                    }
                });
        return out;
    }
}
