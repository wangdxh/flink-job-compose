package com.kedacom.flinketlgraph.source;

import java.util.Arrays;

public class KafkaFullData
{
    private byte[] key;
    private byte[] value;
    private String topic;
    private int partition;
    private long offset;

    public KafkaFullData(byte[] key, byte[] value, String topic, int partition, long offset)
    {
        this.key = key;
        this.value = value;
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
    }

    public KafkaFullData()
    {
    }

    public byte[] getKey()
    {
        return key;
    }

    public void setKey(byte[] key)
    {
        this.key = key;
    }

    public byte[] getValue()
    {
        return value;
    }

    public void setValue(byte[] value)
    {
        this.value = value;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public int getPartition()
    {
        return partition;
    }

    public void setPartition(int partition)
    {
        this.partition = partition;
    }

    public long getOffset()
    {
        return offset;
    }

    public void setOffset(long offset)
    {
        this.offset = offset;
    }

    @Override
    public String toString()
    {
        return "KafkaFullData{" +
                "key=" + Arrays.toString(key) +
                ", value=" + Arrays.toString(value) +
                ", topic='" + topic + '\'' +
                ", partition=" + partition +
                ", offset=" + offset +
                '}';
    }
}
