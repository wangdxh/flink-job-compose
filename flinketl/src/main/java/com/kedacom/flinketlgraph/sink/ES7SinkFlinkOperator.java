package com.kedacom.flinketlgraph.sink;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.*;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.connectors.elasticsearch.ActionRequestFailureHandler;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkBase;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink;
import org.apache.flink.util.ExceptionUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.util.concurrent.EsRejectedExecutionException;
import org.elasticsearch.common.xcontent.XContentType;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;

public class ES7SinkFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinksink_elasticsearch7",
            "elasticsearch 7 sink",
            CommonUtils.Sink,
            CommonUtils.readfile("jsonfilesgraph/es7sinkspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L, Arrays.asList(CommonUtils.esindexbean),
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
        return source.addSink(createes7(sinkconfig));
    }

    @Override
    public DataStreamSink<Object> CreateSink(SingleOutputStreamOperator<Object> trans, JobOperator sinkconfig) throws Exception
    {
        return trans.addSink(createes7(sinkconfig));
    }*/

    @Override
    public DataStreamSink<Object> CreateSink(DataStream<Object> trans, Graphnode sinkconfig, Graph graph) throws Exception
    {
        return trans.addSink(createes7(sinkconfig));
    }

    public ElasticsearchSink<Object> createes7(Graphnode sinkcfg) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        Es7sinkspec spec = mapper.convertValue(sinkcfg.getElementconfig(), Es7sinkspec.class);
        boolean throwexception = spec.getThrowesexception();

        List<HttpHost> hosts = CommonUtils.getClusterAddresses(spec.getHosts());
        ElasticsearchSink.Builder<Object> esSinkBuilder =
                new ElasticsearchSink.Builder<>(hosts, new ElasticsearchSinkFunctionImpl());

        esSinkBuilder.setBulkFlushMaxActions(spec.getBatchnums().intValue());
        esSinkBuilder.setFailureHandler(new ActionRequestFailureHandlerImp(throwexception));

        if (spec.getFlushinterval() > 0){
            esSinkBuilder.setBulkFlushInterval(spec.getFlushinterval());
        }
        esSinkBuilder.setBulkFlushBackoff(true);
        esSinkBuilder.setBulkFlushBackoffDelay(100);
        esSinkBuilder.setBulkFlushBackoffType(ElasticsearchSinkBase.FlushBackoffType.CONSTANT);
        esSinkBuilder.setBulkFlushBackoffRetries(spec.getRetries().intValue());

        return esSinkBuilder.build();
    }

    static class ElasticsearchSinkFunctionImpl implements ElasticsearchSinkFunction<Object>{
        private IndexRequest indexRequest(Object o) {
                if (!(o instanceof ESIndexBean)){
                    throw new RuntimeException("es7 input type is not ESIndexBean");
                }
                ESIndexBean b = (ESIndexBean)o;
            return Requests.indexRequest()
                    .index(b.getIndex())
                    .type(b.getType())
                    .id(b.getId())
                    .source(b.getSource(), XContentType.JSON);
        }
        @Override
        public void process(Object o, RuntimeContext runtimeContext, RequestIndexer requestIndexer) {
            requestIndexer.add(indexRequest(o));
        }
    }

    static class ActionRequestFailureHandlerImp implements ActionRequestFailureHandler {
        private boolean bthrowexception;
        public ActionRequestFailureHandlerImp(boolean bthrow){
            this.bthrowexception = bthrow;
        }

        @Override
        public void onFailure(ActionRequest action, Throwable failure,
                              int restStatusCode, RequestIndexer indexer) throws Throwable {
            IndexRequest req = (IndexRequest)action;
            System.out.println(req.id());

            // 异常1: ES队列满了(Reject异常)，放回队列
            if(ExceptionUtils.findThrowable(failure, EsRejectedExecutionException.class).isPresent()){
                indexer.add(action);

                // 异常2: ES超时异常(timeout异常)，放回队列
            }else if(ExceptionUtils.findThrowable(failure, SocketTimeoutException.class).isPresent()){
                indexer.add(action);

                // 异常3: ES语法异常，丢弃数据，记录日志
            }else if(ExceptionUtils.findThrowable(failure, ElasticsearchParseException.class).isPresent()){
                failure.printStackTrace();
                //log.error("Sink to es exception ,exceptionData: {} ,exceptionStackTrace: {}",action.toString(),org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(failure));
                // 异常4: 其他异常，丢弃数据，记录日志
            }else{
                if (bthrowexception){
                    throw failure;
                }else{
                    failure.printStackTrace();
                }
            }
        }}

}

