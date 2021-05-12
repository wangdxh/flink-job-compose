package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.ElementDescriptor;
import com.kedacom.flinketlgraph.json.Elesingleportinfo;
import com.kedacom.flinketlgraph.json.Graph;
import com.kedacom.flinketlgraph.json.Graphnode;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import java.util.Arrays;

public class DelayFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinktransform_delay",
            "delay transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/delaytransformspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.object),
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
    public SingleOutputStreamOperator<Object> CreateTransform(DataStream<Object> trans, Graphnode transconfig, Graph graph) throws Exception
    {
        return trans.flatMap(new DelayFlatMap(transconfig)).returns(Types.GENERIC(Object.class));
    }
}
