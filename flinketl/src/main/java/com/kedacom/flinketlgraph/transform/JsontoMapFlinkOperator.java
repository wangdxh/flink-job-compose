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

public class JsontoMapFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinktransform_jsontomap",
            "json transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/jsontomaptransformspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.stringtype, CommonUtils.bytearray),
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

    /*@Override
    public SingleOutputStreamOperator<Object> CreateTransform(DataStreamSource<Object> source, JobOperator transconfig) throws Exception
    {
        return source.flatMap(new JsontoMapFlatmap(transconfig)).returns(Types.GENERIC(Object.class));
    }

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(SingleOutputStreamOperator<Object> trans, JobOperator transconfig) throws Exception
    {
        return trans.flatMap(new JsontoMapFlatmap(transconfig)).returns(Types.GENERIC(Object.class));
    }*/
    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(DataStream<Object> trans, Graphnode transconfig, Graph graph) throws Exception
    {
        return trans.flatMap(new JsontoMapFlatmap(transconfig)).returns(Types.GENERIC(Object.class));
    }
}
