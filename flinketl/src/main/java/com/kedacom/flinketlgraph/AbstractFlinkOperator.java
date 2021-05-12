package com.kedacom.flinketlgraph;

import com.kedacom.flinketlgraph.json.Graph;
import com.kedacom.flinketlgraph.json.Graphnode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;
import java.util.Map;

public abstract class AbstractFlinkOperator implements IFlinkOperator
{
    @Override
    public DataStreamSource<Object> CreateSource(StreamExecutionEnvironment env, Graphnode src, Graph graph)  throws Exception
    {
        return null;
    }

    /*@Override
    public SingleOutputStreamOperator<Object> CreateTransform(DataStreamSource<Object> source, JobOperator transconfig) throws Exception
    {
        return null;
    }

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(SingleOutputStreamOperator<Object> trans, JobOperator transconfig) throws Exception{
        return null;
    }*/

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(List<DataStream<Object>> translist, Graphnode transconfig, Graph graph) throws Exception
    {
        throw new Exception("CreateTransform1 not implenment");
    }

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(DataStream<Object> trans, Graphnode transconfig, Graph graph) throws Exception
    {
        throw new Exception("CreateTransform2 not implenment");
    }

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(DataStream<Object> trans, Graphnode transconfig,
                                                       Graph graph, Map<String, DataStream<Object>> outmap) throws Exception{
        throw new Exception("CreateTransform3 not implenment");
    }

    /*@Override
    public DataStreamSink<Object> CreateSink(DataStreamSource<Object> source, JobOperator sinkconfig) throws Exception{
        return null;
    }

    @Override
    public DataStreamSink<Object> CreateSink(SingleOutputStreamOperator<Object> trans, JobOperator sinkconfig) throws Exception
    {
        return null;
    }*/

    @Override
    public DataStreamSink<Object> CreateSink(DataStream<Object> source, Graphnode sinkconfig, Graph graph) throws Exception
    {
        return null;
    }

}
