-- aaa is aaa
set aaa = 123;
SET bbb = 345;
create function MyFunction as 'com.kedacom.flinksqlfunction.MyUdfIntToString';

create TABLE kafkaprotobuf (
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
			   'connector' = 'kafka',  -- 指定连接类型是kafka
		    --'version' = 'universal',  -- 与我们之前Docker安装的kafka版本要一致
			    'topic' = 'protobuftest2', -- 之前创建的topic
			--'update-mode' = 'append',
			'scan.startup.mode' = 'earliest-offset',
 			'properties.bootstrap.servers' = '10.67.65.25:19092',
 			 -- 'properties.bootstrap.servers' = '10.67.65.25:9092'  protoBytes5

			'format' = 'protobuf',
			'protobuf.descfilepath'='E:/bigdata/flinketl/flinksql/src/main/resources/fieldteyp.desc',
			'protobuf.protoclass'='FiledType'
);

CREATE table print_table WITH ('connector' = 'print')
LIKE kafkaprotobuf (EXCLUDING ALL);

insert into print_table select * from kafkaprotobuf;

