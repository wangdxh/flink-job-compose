package com.kedacom.flinketlgraph.sink;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.ElementDescriptor;
import com.kedacom.flinketlgraph.json.Elesingleportinfo;
import com.kedacom.flinketlgraph.json.Graph;
import com.kedacom.flinketlgraph.json.Graphnode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;

import java.util.Arrays;

public class JdbcSinkFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinksink_jdbc",
            "jdbc sink",
            CommonUtils.Sink,
            CommonUtils.readfile("jsonfilesgraph/jdbcsinkspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L, Arrays.asList(CommonUtils.listobject),
                            "input 0", "datainput"
                    )
            ),
            Arrays.asList()
    );

    @Override
    public ElementDescriptor getoperatordesc()
    {
        return this.desc;
    }

    /*@Override
    public DataStreamSink<Object> CreateSink(DataStreamSource<Object> source, JobOperator sinkconfig) throws Exception
    {
        return source.addSink(new JdbcSink(sinkconfig));
    }

    @Override
    public DataStreamSink<Object> CreateSink(SingleOutputStreamOperator<Object> trans, JobOperator sinkconfig) throws Exception
    {
        return trans.addSink(new JdbcSink(sinkconfig));
    }*/

    @Override
    public DataStreamSink<Object> CreateSink(DataStream<Object> trans, Graphnode sinkconfig, Graph graph) throws Exception
    {
        return trans.addSink(new JdbcSink(sinkconfig));
    }
}

