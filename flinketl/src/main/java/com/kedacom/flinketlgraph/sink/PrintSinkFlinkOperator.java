package com.kedacom.flinketlgraph.sink;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.ElementDescriptor;
import com.kedacom.flinketlgraph.json.Elesingleportinfo;
import com.kedacom.flinketlgraph.json.Graph;
import com.kedacom.flinketlgraph.json.Graphnode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.Arrays;

public class PrintSinkFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinksink_print",
            "print element",
            CommonUtils.Sink,
            CommonUtils.readfile("jsonfilesgraph/rulerspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.object),
                            "input 0",
                            "datainput"
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
    public DataStreamSink<Object> CreateSink(DataStream<Object> trans, Graphnode sinkconfig, Graph graph) throws Exception
    {
        return trans.addSink(CreateSink());
    }

    private SinkFunction<Object> CreateSink(){
        return new SinkFunction<Object>()
        {
            private static final long serialVersionUID = -5086526073569483945L;

            @Override
            public void invoke(Object value, Context context) throws Exception
            {
                System.out.println("hahaha---" + value.toString());
            }
        };
    }
}

