package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.*;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;

public class ConnectOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinktransform_connect",
            "connect transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/rulerspec.json"),
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
        DataStream<Object> right = translist.get(1);

        StreamExecutionEnvironment env = trans.getExecutionEnvironment();

        ObjectMapper mapper = new ObjectMapper();
        Rulerspec spec = mapper.convertValue(transconfig.getElementconfig(), Rulerspec.class);

        SingleOutputStreamOperator<Object> out = trans.connect(right)
                .flatMap(new ConnectCoFlatMap())
                .returns(Types.GENERIC(Object.class));
        return out;
    }
}
