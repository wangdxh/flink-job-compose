CREATE TABLE jsonfileread (
    i32 int,
	enums string,
    i64 BIGINT,
    si32 int,
    si64 BIGINT,
    sf32 int,
    sf64 BIGINT,

    f float,
    d double,
    bl BOOLEAN,
    ui32 int,
    ui64 BIGINT,
    fi32 int,
    fi64 BIGINT,
    createTime BIGINT,
    bs bytes,
    sg ARRAY<varchar>,
    person ARRAY<ROW<name varchar, age integer>>
	) with (
			'connector' = 'filesystem',  -- 指定连接类型是filesystem
			'path'='file:///Users/wang/Downloads/protobuftojson.json',
			--'update-mode' = 'append',
			'format' = 'json' -- json格式，和topic中的消息格式保持一致
);


CREATE TABLE kafkaprotobuf with (
			   'connector' = 'kafka',  -- 指定连接类型是kafka
		    --'version' = 'universal',  -- 与我们之前Docker安装的kafka版本要一致
			    'topic' = 'protobuftest2', -- 之前创建的topic
			--'update-mode' = 'append',
			'scan.startup.mode' = 'earliest-offset',
 			'properties.bootstrap.servers' = '127.0.0.1:9092',
 			 -- 'properties.bootstrap.servers' = '10.67.65.25:9092'  protoBytes5

			'format' = 'protobuf',
			'protobuf.descfilepath'='/Users/wang/Desktop/GitHub/flinketl/flinksql/src/main/resources/fieldteyp.desc',
			'protobuf.protoclass'='FiledType'
)LIKE jsonfileread (EXCLUDING ALL);

INSERT INTO kafkaprotobuf select * from jsonfileread;
