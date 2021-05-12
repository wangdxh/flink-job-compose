package com.kedacom.flinketlgraph.sink;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.ElementDescriptor;
import com.kedacom.flinketlgraph.json.Elesingleportinfo;
import com.kedacom.flinketlgraph.json.Graph;
import com.kedacom.flinketlgraph.json.Graphnode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;

import java.util.Arrays;

public class DiscardSinkFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinksink_discard",
            "a discard element, just discard every input",
            CommonUtils.Sink,
            CommonUtils.readfile("jsonfilesgraph/rulerspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L, Arrays.asList(CommonUtils.object),
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
    public DataStreamSink<Object> CreateSink(DataStreamSource<Object> source, JobOperator sinkconfig)
    {
        return source.addSink(CreateSink());
    }

    @Override
    public DataStreamSink<Object> CreateSink(SingleOutputStreamOperator<Object> trans, JobOperator sinkconfig)
    {
        return trans.addSink(CreateSink());
    }*/
    @Override
    public DataStreamSink<Object> CreateSink(DataStream<Object> trans, Graphnode sinkconfig, Graph graph)
    {
        return trans.addSink(new DiscardingSink<>());
    }
}

