package com.kedacom.flinketlgraph.source;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.ElementDescriptor;
import com.kedacom.flinketlgraph.json.Elesingleportinfo;
import com.kedacom.flinketlgraph.json.Graph;
import com.kedacom.flinketlgraph.json.Graphnode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

public class RedisSourceFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinksource_redis",
            "redis source",
            CommonUtils.Source,
            CommonUtils.readjsonschemafile("jsonfilesgraph/redissourcespec.json", "jsonfilesgraph/redissinkspec.json"),
            Arrays.asList(),
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
    public DataStreamSource<Object> CreateSource(StreamExecutionEnvironment env, Graphnode src, Graph graph) throws Exception
    {
        return env.addSource(new RedisSource(src));
    }
}
