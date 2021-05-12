CREATE TABLE user_behavior_mysql (
              channel varchar  ,
              page varchar
            )WITH(
              'connector' = 'jdbc', -- 连接方式
              'url' = 'jdbc:mysql://localhost:3307/testdb', -- jdbc的url
              'table-name' = 'user_behavior',  -- 表名
              'driver' = 'com.mysql.jdbc.Driver', -- 驱动名字，可以不填，会自动从上面的jdbc url解析
              'username' = 'root', -- 顾名思义 用户名
              'password' = 'kedacom' , -- 密码
              'scan.fetch-size' = '100'
              --'sink.buffer-flush.interval' = '2',
              --'sink.buffer-flush.max-rows' = '100'
);

CREATE TABLE print_table WITH ('connector' = 'print')
LIKE user_behavior_mysql (EXCLUDING ALL);

INSERT INTO print_table select * from user_behavior_mysql;
