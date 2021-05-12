package com.kedacom.flinketlgraph.sink;

import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkKafkaPartitioner;
import org.apache.flink.util.Preconditions;

import java.util.concurrent.atomic.AtomicInteger;


public class KafkaRoundRobinParttioner<T> extends FlinkKafkaPartitioner<T> {
    private static final long serialVersionUID = 2751909491046263214L;
    private AtomicInteger number = new AtomicInteger(0);
    public KafkaRoundRobinParttioner() {

    }

    public int partition(T record, byte[] key, byte[] value, String targetTopic, int[] partitions) {
        Preconditions.checkArgument(partitions != null && partitions.length > 0, "Partitions of the target topic is empty.");
        return partitions[this.number.incrementAndGet() % partitions.length];
    }

    public boolean equals(Object o) {
        return this == o || o instanceof KafkaRoundRobinParttioner;
    }

    public int hashCode() {
        return KafkaRoundRobinParttioner.class.hashCode();
    }
}