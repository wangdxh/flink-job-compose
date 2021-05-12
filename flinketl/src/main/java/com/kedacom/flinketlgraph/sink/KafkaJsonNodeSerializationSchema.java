package com.kedacom.flinketlgraph.sink;

import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaJsonNodeSerializationSchema implements SerializationSchema<Object>
{
    private static final long serialVersionUID = 3328730263248911608L;

    private ObjectMapper mapper;
    @Override
    public byte[] serialize(Object node) {
        if (null == mapper){
            mapper = new ObjectMapper();
        }
        byte[] ret = null;
        try
        {
            ret = mapper.writeValueAsBytes(node);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return ret;
    }
}
