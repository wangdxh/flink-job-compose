package com.kedacom.flinketlgraph.sink;

import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Redissinkspec;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class RedisKedaSink extends RichSinkFunction<Object> {
    private static final long serialVersionUID = 829936986178306610L;

    private static final Logger LOG = LoggerFactory.getLogger(RedisKedaSink.class);
    private Redissinkspec spec;

    private transient RedisClient redisclient;
    private transient RedisCommands<String, String> sync;
    private transient StatefulRedisConnection<String, String> connection;

    public RedisKedaSink(Graphnode trans) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        this.spec = mapper.convertValue(trans.getElementconfig(), Redissinkspec.class);

        if (spec.getCluster() != null) {
            throw new Exception("now redis cluster is not support");
        }
    }
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        RedisURI url = new RedisURI();
        Redissinkspec redisinfo = this.spec;
        url.setHost(redisinfo.getRedishost());
        url.setPort(redisinfo.getRedisport().intValue());
        url.setDatabase(redisinfo.getDbindex().intValue());
        if (redisinfo.getPassword()!=null && redisinfo.getPassword().length()>0){
            url.setPassword(redisinfo.getPassword());
        }
        redisclient = RedisClient.create(url);
        connection = redisclient.connect();
        sync = connection.sync();
    }

    @Override
    public void invoke(Object input, Context context) throws Exception {
        if (! (input instanceof List)){
            throw new Exception("redis input data is not of list");
        }

        for(Object item : (List) input){
            RedisBean o = (RedisBean)item;
            String key = o.getKey();
            String value = o.getValue();
            switch (RedisBean.Command.valueOf(o.getCommand())) {
                case SADD:
                    this.sync.sadd(key, value);
                    break;
                case SET:
                    this.sync.set(key, value);
                    break;
                case ZADD:
                    this.sync.zadd(key, Double.parseDouble(value), o.getAdditionalkey());
                    break;
                case HSET:
                    this.sync.hset(key, o.getAdditionalkey(), value);
                    break;
                default:
                    throw new IllegalArgumentException("Cannot process such data type just support SET SADD ZADD HSET");
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (connection != null) {
            connection.close();
        }
        if (redisclient != null) {
            redisclient.shutdown();
        }
    }
}
