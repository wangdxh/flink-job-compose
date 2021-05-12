-- aaa is aaa
set aaa = 123;
SET bbb = 345;
create  function MyStringFunction as 'com.kedacom.flinksqlfunction.MyUdfIntToString';

create TABLE kafkaprotobuf (
	i32 int,
	enums string
	) with (
			   'connector' = 'kafka',  -- 指定连接类型是kafka
		    --'version' = 'universal',  -- 与我们之前Docker安装的kafka版本要一致
			    'topic' = 'protobuftest2', -- 之前创建的topic
			--'update-mode' = 'append',
			'scan.startup.mode' = 'latest-offset',
 			'properties.bootstrap.servers' = '10.67.65.25:19092',
 			 -- 'properties.bootstrap.servers' = '10.67.65.25:9092'  protoBytes5

			'format' = 'protobuf',
			'protobuf.descfilepath'='E:/bigdata/flinketl/flinksql/src/main/resources/fieldteyp.desc',
			'protobuf.protoclass'='FiledType'
);

CREATE table print_table (
    i32 int,
	enums string
) WITH ('connector' = 'print');

insert into print_table select i32, MyStringFunction(enums) from kafkaprotobuf;

