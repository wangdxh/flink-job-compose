package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.ElementDescriptor;
import com.kedacom.flinketlgraph.json.Elesingleportinfo;
import com.kedacom.flinketlgraph.json.Graph;
import com.kedacom.flinketlgraph.json.Graphnode;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import java.util.Arrays;
import java.util.List;

public class RulerOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinktransform_ruler",
            "ruler transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/rulerspec.json"),
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
                            Arrays.asList(CommonUtils.mapobject),
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

        //create ruler BroadcastProcessFunction funtion
        MapStateDescriptor<String, String> broadstatdesc = new MapStateDescriptor<String, String>("broadstatdesc",
                String.class, String.class);
        RulerProcess broadprocess = new RulerProcess(broadstatdesc);

        DataStream<Object> rulerstream = translist.get(1);
        SingleOutputStreamOperator<Object> out = trans.connect(rulerstream.broadcast(broadstatdesc))
                .process(broadprocess)
                .returns(Types.GENERIC(Object.class));
        return out;
    }

}
