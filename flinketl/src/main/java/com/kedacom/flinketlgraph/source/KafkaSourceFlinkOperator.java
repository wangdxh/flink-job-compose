package com.kedacom.flinketlgraph.source;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.*;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class KafkaSourceFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinksource_kafka",
            "kafka source",
            CommonUtils.Source,
            CommonUtils.readfile("jsonfilesgraph/kafkaspecconsumer.json"),
            Arrays.asList(),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.bytearray, CommonUtils.stringtype, CommonUtils.kafkafulldata),
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
        ObjectMapper mapper = new ObjectMapper();
        Kafkaspecconsumer spec = mapper.convertValue(src.getElementconfig(), Kafkaspecconsumer.class);

        Properties pro = new Properties();
        pro.setProperty("bootstrap.servers", spec.getBrokers());
        pro.setProperty("group.id", spec.getGroupid());
        pro.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        pro.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        pro.setProperty("auto.offset.reset", "earliest");

        pro.setProperty("flink.partition-discovery.interval-millis", "9000000");
        pro.setProperty("max.poll.records", "1000");
        pro.setProperty("auto.commit.interval.ms", "1000");
        pro.setProperty("request.timeout.ms", "30000");

        List<String> topics = Arrays.asList(spec.getTopics().split(","));
        FlinkKafkaConsumer consumer = null;
        switch (CommonUtils.getFirstOutputtype(src))
        {
            case CommonUtils.bytearray:
            {
                consumer = new FlinkKafkaConsumer(topics, new KafkaByteArrayDeserializationShema<byte[]>(), pro);
                break;
            }
            case CommonUtils.stringtype:
            {
                consumer = new FlinkKafkaConsumer(topics, new SimpleStringSchema(), pro);
                break;
            }
            case CommonUtils.kafkafulldata:
            {
                consumer = new FlinkKafkaConsumer(topics, new KafkaFullDataDeserializationShema(), pro);
                break;
            }
        }
        if (spec.getStartfrom() != null){
            switch (spec.getStartfrom()){
                case EARLIEST:{
                    consumer.setStartFromEarliest();
                    break;
                }
                case LATEST:{
                    consumer.setStartFromLatest();
                    break;
                }
                //consumer.setStartFromTimestamp(0);
            }
        }
        //consumer.setStartFromEarliest();
        return env.addSource(consumer);
    }
}
