-- aaa is aaa
SET aaa = 123;
SET bbb = 345;

CREATE TABLE kafkaprotobuf (
	i32 int,
	enums string
	) with (
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
);

CREATE TABLE rediswrite(
    i32 string,
    enums string
) WITH (
    'connector' = 'redis',
    'host'='localhost',
    'port'='6379'
);

INSERT INTO rediswrite select CAST(i32 as varchar), enums from kafkaprotobuf;

