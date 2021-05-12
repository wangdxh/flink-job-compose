package com.kedacom.flinketlgraph.sink;

import org.apache.flink.api.common.serialization.SerializationSchema;

public class KafkaByteArraySerializationSchema implements SerializationSchema<byte[]>
{
    private static final long serialVersionUID = -9041635653417556141L;

    @Override
    public byte[] serialize(byte[] bytes) {
        return bytes;
    }
}
