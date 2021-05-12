package com.kedacom.flinksql.table;

import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.sink.SinkFunctionProvider;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.StringData;

public class RedisDynamicTableSink implements DynamicTableSink {

    private ReadableConfig options;

    public RedisDynamicTableSink(ReadableConfig options) {
        this.options = options;
    }

    @Override
    public ChangelogMode getChangelogMode(ChangelogMode changelogMode) {
        return ChangelogMode.insertOnly();
    }

    @Override
    public SinkRuntimeProvider getSinkRuntimeProvider(Context context) {
        String host = options.get(RedisDynamicTableSourceFactory.host);
        FlinkJedisPoolConfig.Builder builder = new FlinkJedisPoolConfig.Builder().setHost(host);
        Integer port = options.get(RedisDynamicTableSourceFactory.port);
        if(port != null){
            builder.setPort(port);
        }
        FlinkJedisPoolConfig build = builder.build();
        RedisMapper<RowData> stringRedisMapper = new RedisMapper<RowData>() {
            @Override
            public RedisCommandDescription getCommandDescription() {
                return new RedisCommandDescription(RedisCommand.SET);
            }

            @Override
            public String getKeyFromData(RowData rowData) {
                StringData string = rowData.getString(0);
                return string.toString();
            }

            @Override
            public String getValueFromData(RowData rowData) {
                String s = rowData.toString();
                return s;
            }
        };
        RedisSink<RowData> stringRedisSink = new RedisSink<>(build, stringRedisMapper);
        return SinkFunctionProvider.of(stringRedisSink);
    }

    @Override
    public DynamicTableSink copy() {
        return new RedisDynamicTableSink(this.options);
    }

    @Override
    public String asSummaryString() {
        return "my_redis_sink";
    }
}