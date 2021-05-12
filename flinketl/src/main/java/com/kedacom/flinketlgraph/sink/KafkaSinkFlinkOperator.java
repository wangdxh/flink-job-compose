package com.kedacom.flinketlgraph.sink;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.*;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

public class KafkaSinkFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinksink_kafka",
            "kafka",
            CommonUtils.Sink,
            CommonUtils.readfile("jsonfilesgraph/kafkaspecproducer.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.object, CommonUtils.bytearray, CommonUtils.stringtype),
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
        return source.addSink(CreateProducer(sinkconfig));
    }

    @Override
    public DataStreamSink<Object> CreateSink(SingleOutputStreamOperator<Object> trans, JobOperator sinkconfig)
    {
        return trans.addSink(CreateProducer(sinkconfig));
    }*/

    @Override
    public DataStreamSink<Object> CreateSink(DataStream<Object> trans, Graphnode sinkconfig, Graph graph) throws Exception
    {
        return trans.addSink(CreateProducer(sinkconfig));
    }

    private FlinkKafkaProducer CreateProducer(Graphnode sinkconfig) throws Exception{
        Object objspec = sinkconfig.getElementconfig();
        ObjectMapper mapper = new ObjectMapper();
        Kafkaspecproducer spec;

        spec = mapper.convertValue(objspec, Kafkaspecproducer.class);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", spec.getBrokers());
        properties.setProperty("partitioner.class","org.apache.kafka.clients.producer.internals.DefaultPartitioner");
        properties.setProperty("acks", "1");
        properties.setProperty("retries", "3");
        properties.setProperty("timeout.ms", "30000");
        properties.setProperty("linger.ms", "100");
        properties.setProperty("request.timeout.ms", "10000");

        switch (CommonUtils.getFirstInputtype(sinkconfig)){
            case CommonUtils.bytearray:{
                return new FlinkKafkaProducer(spec.getTopic(), new KafkaByteArraySerializationSchema(), properties,
                        Optional.of(new KafkaRoundRobinParttioner()));
            }
            case CommonUtils.stringtype:{
                return new FlinkKafkaProducer(spec.getTopic(), new SimpleStringSchema(), properties,
                        Optional.of(new KafkaRoundRobinParttioner()));
            }
            case CommonUtils.object:{
                return new FlinkKafkaProducer(spec.getTopic(), new KafkaJsonNodeSerializationSchema(), properties,
                        Optional.of(new KafkaRoundRobinParttioner()));
            }
        }
        return null;
    }
}
