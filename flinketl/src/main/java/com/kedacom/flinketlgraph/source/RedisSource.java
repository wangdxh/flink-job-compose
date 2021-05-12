package com.kedacom.flinketlgraph.source;

import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Redissinkspec;
import com.kedacom.flinketlgraph.json.Redissourcespec;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisSource  extends RichSourceFunction<Object> {
    private static final Logger mylogger = LoggerFactory.getLogger(RedisSource.class);

    private Redissourcespec spec;
    private Graphnode joboperator;

    private volatile boolean brunning = true;
    private transient RedisClient redisclient;
    private transient RedisCommands<String, String> sync;
    private transient StatefulRedisConnection<String, String> connection;

    public RedisSource(Graphnode trans) throws Exception{
        this.joboperator = trans;
        ObjectMapper mapper = new ObjectMapper();
        this.spec = mapper.convertValue(trans.getElementconfig(), Redissourcespec.class);
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
    public void run(SourceContext<Object> ctx) throws Exception {
        Redissourcespec.Command command = this.spec.getCommand();
        Object value = null;
        while (brunning){
            if (value==null){
                switch (command){
                    case GET:
                        value = sync.get(this.spec.getRediskey());
                        break;
                    case HGET:
                        value = sync.hget(this.spec.getRediskey(), this.spec.getAdditionalkey());
                        break;
                    case HGETALL:
                        value = sync.hgetall(this.spec.getRediskey());
                        break;
                    default:
                        break;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("key", this.spec.getRediskey());
                map.put("additionkey", this.spec.getAdditionalkey());
                map.put("value", value);
                ctx.collect(map);
            }

            if (this.spec.getIncrement()>0){
                for (int i=0; i<this.spec.getIncrement() && brunning; i++){
                    TimeUnit.SECONDS.sleep(1);
                }
                value = null;
            }else {
                TimeUnit.MILLISECONDS.sleep(100);
            }
        }
    }

    @Override
    public void cancel() {
        brunning = false;
    }

}
