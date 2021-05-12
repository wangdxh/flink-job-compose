package com.kedacom.flinketlgraph;

import com.kedacom.flinketlgraph.json.ElementDescriptor;
import com.kedacom.flinketlgraph.json.Graph;
import com.kedacom.flinketlgraph.json.Graphnode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;
import java.util.Map;

public interface IFlinkOperator
{
    ElementDescriptor getoperatordesc();
    DataStreamSource<Object> CreateSource(StreamExecutionEnvironment env, Graphnode src, Graph graph) throws Exception;
    //SingleOutputStreamOperator<Object> CreateTransform(DataStreamSource<Object> source, JobOperator transconfig) throws Exception;
    //SingleOutputStreamOperator<Object> CreateTransform(SingleOutputStreamOperator<Object> trans, JobOperator transconfig) throws Exception;

    SingleOutputStreamOperator<Object> CreateTransform(List<DataStream<Object>> translist, Graphnode transconfig, Graph graph) throws Exception;
    SingleOutputStreamOperator<Object> CreateTransform(DataStream<Object> trans, Graphnode transconfig, Graph graph) throws Exception;
    SingleOutputStreamOperator<Object> CreateTransform(DataStream<Object> trans, Graphnode transconfig,
                                                       Graph graph, Map<String, DataStream<Object>> outmap) throws Exception;

    //DataStreamSink<Object> CreateSink(DataStreamSource<Object> source, JobOperator sinkconfig) throws Exception;
    //DataStreamSink<Object> CreateSink(SingleOutputStreamOperator<Object> trans, JobOperator sinkconfig) throws Exception;
    DataStreamSink<Object> CreateSink(DataStream<Object> trans, Graphnode sinkconfig, Graph graph) throws Exception;
}
