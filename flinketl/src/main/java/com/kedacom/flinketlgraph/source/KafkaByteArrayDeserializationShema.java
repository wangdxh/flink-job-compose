package com.kedacom.flinketlgraph.source;

import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;

import java.io.IOException;

public class KafkaByteArrayDeserializationShema<T> extends AbstractDeserializationSchema<byte[]>
{
    private static final long serialVersionUID = 2058554269574956907L;

    @Override
    public byte[] deserialize(byte[] bytes) throws IOException
    {
        return bytes;
    }
}