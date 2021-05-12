package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Redissinkspec;
import com.kedacom.flinketlgraph.json.Redistransformspec;
import com.kedacom.flinketlgraph.sink.RedisBean;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class RedisMapFunction extends RichMapFunction<Object, Object> {
    private Redistransformspec spec;

    private transient RedisClient redisclient;
    private transient RedisCommands<String, String> sync;
    private transient StatefulRedisConnection<String, String> connection;

    public RedisMapFunction(Graphnode trans) throws Exception {
        //this.joboperator = trans;
        ObjectMapper mapper = new ObjectMapper();
        this.spec = mapper.convertValue(trans.getElementconfig(), Redistransformspec.class);
        if (this.spec.getRedisinfo() == null){
            this.spec.setRedisinfo(new Redissinkspec());
        }
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        RedisURI url = new RedisURI();
        Redissinkspec redisinfo = this.spec.getRedisinfo();
        url.setHost(redisinfo.getRedishost());
        url.setPort(redisinfo.getRedisport().intValue());
        url.setDatabase(redisinfo.getDbindex().intValue());
        if (redisinfo.getPassword()!=null && redisinfo.getPassword().length()>0){
            url.setPassword(redisinfo.getPassword());
        }
        redisclient = RedisClient.create(url);
        RedisCodec valueCodec = new StringCodec();
        if (redisinfo.getValuecodec()== Redissinkspec.Valuecodec.BYTEARRAY){
            valueCodec = new ByteArrayCodec();
        }
        connection = redisclient.connect(RedisCodec.of(new StringCodec(), valueCodec));
        sync = connection.sync();
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
        if (redisclient != null) {
            redisclient.shutdown();
        }
        super.close();
    }

    @Override
    public Object map(Object o) throws Exception {
        if (!(o instanceof RedisTransInputBean)){
            throw new Exception("the input type is not RedisTransInputBean");
        }

        RedisTransInputBean input = (RedisTransInputBean)o;
        RedisTransResultBean outbean = new RedisTransResultBean();
        outbean.extradata = input.extradata;
        for(RedisBean item : input.redisinput){
            String key = item.getKey();
            String value = item.getValue();
            Object temp = null;
            switch (RedisBean.Command.valueOf(item.getCommand())) {
                case HGET:
                    temp = sync.hget(item.getKey(), item.getAdditionalkey());
                    break;
                case HGETALL:
                    temp = sync.hgetall(item.getKey());
                    break;
                case GET:
                    temp = sync.get(item.getKey());
                    break;
                case SISMEMBER:
                    temp = sync.sismember(item.getKey(), item.getValue());
                    break;
                default:
                    break;
            }
            outbean.getRedisresult().add(temp);
        }
        return outbean;
    }
}
