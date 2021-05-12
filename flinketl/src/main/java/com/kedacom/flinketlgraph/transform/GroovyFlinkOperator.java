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

public class GroovyFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinktransform_groovy",
            "groovy transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/groovytransformspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.mapobject, CommonUtils.object),
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

    /*@Override
    public SingleOutputStreamOperator<Object> CreateTransform(DataStreamSource<Object> source, JobOperator transconfig) throws Exception
    {
        return source.flatMap(new GroovyFlatmap(transconfig)).returns(Types.GENERIC(Object.class));
    }

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(SingleOutputStreamOperator<Object> trans, JobOperator transconfig) throws Exception
    {
        return trans.flatMap(new GroovyFlatmap(transconfig)).returns(Types.GENERIC(Object.class));
    }*/

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(DataStream<Object> trans, Graphnode transconfig, Graph graph) throws Exception
    {
        return trans.flatMap(new GroovyFlatmap(transconfig, graph)).returns(Types.GENERIC(Object.class));
    }
}
