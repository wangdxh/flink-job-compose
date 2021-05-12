package com.kedacom.flinketlgraph.source;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

@PublicEvolving
public class KafkaFullDataDeserializationShema implements KeyedDeserializationSchema<KafkaFullData>
{
    private static final long serialVersionUID = 376053924222303714L;

    public KafkaFullDataDeserializationShema() {
    }

    @Override
    public KafkaFullData deserialize(byte[] messageKey, byte[] message, String topic, int partition, long offset)
    {
        return new KafkaFullData(messageKey, message, topic, partition, offset);
    }

    @Override
    public boolean isEndOfStream(KafkaFullData nextElement) {
        return false;
    }

    @Override
    public TypeInformation<KafkaFullData> getProducedType() {
        return TypeExtractor.getForClass(KafkaFullData.class);
    }
}
