/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kedacom.flinksql;

import com.kedacom.flinksql.json.Chkpointcfg;
import com.kedacom.flinksql.json.Sqlconfig;
import com.kedacom.flinksql.json.Sqljobconfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.SqlParserException;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import com.kedacom.flinksql.SqlCommandParser.*;
import org.apache.flink.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="http://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class StreamingJob {

    private static final Logger LOG = LoggerFactory.getLogger(StreamingJob.class);

    public static void main(String[] args) throws Exception {
        ParameterTool tool = ParameterTool.fromArgs(args);

        String sqlJobFilePath = tool.get("sqljobfilepath");
        String sqlJobContent = tool.get("sqljobcontent");

        Sqljobconfig sqljobconfig = CommonUtils.getSqlJobFromTool(sqlJobFilePath, sqlJobContent);
        checkSqlConfigIsOk(sqljobconfig);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        setSqlJsonConfigEnv(env, sqljobconfig);

        // Load 用户自定义函数Jar
        List<String> dependJarPaths = sqljobconfig.getSqlconfig().getDependjarpaths();

        AddUserDefineFunctionToEnv(dependJarPaths, env);

        EnvironmentSettings bsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, bsSettings);

        // 从sql文件中 解析sql语句
        List<String> sqlLines = resolveSqlLines(sqljobconfig);

        List<SqlCommandCall> calls = SqlCommandParser.parse(sqlLines);

        for (SqlCommandCall call : calls) {
            callCommand(call, tEnv);
        }

        tEnv.execute(sqljobconfig.getSqlconfig().getJobname());
    }

    private static void checkSqlConfigIsOk(Sqljobconfig sqljobconfig) throws Exception {
        String sqlJsonSchema = CommonUtils.readfile("jsonfilessql/sqljobconfig.json");
        CommonUtils.jsonSchemaValidator(sqlJsonSchema, sqljobconfig);
    }


    private static void setSqlJsonConfigEnv(StreamExecutionEnvironment env, Sqljobconfig sqljobconfig) {
        Sqlconfig sqlconfig = sqljobconfig.getSqlconfig();

        Long jobparallel = sqlconfig.getParallel();
        env.setParallelism(jobparallel.intValue());


        Chkpointcfg chk = sqlconfig.getChkpointcfg();
        if (chk.getEnable()) {
            // get checkpoint from OS's envi..ment,
            String checkpath = System.getenv(CommonUtils.FLINK_CHECKPOINT_PATH);
            System.out.println("======================> checkPath:" + checkpath);
            if (!StringUtils.isNullOrWhitespaceOnly(checkpath)) {
                checkpath += (checkpath.endsWith(File.separator) ? "" : File.separator) + sqlconfig.getJobname();
                env.setStateBackend(new FsStateBackend(checkpath));
                LOG.info("setstatebackend {} to job {}", checkpath, sqlconfig.getJobname());
                System.out.println("======================> checkPath:" + checkpath);
            }

            CheckpointingMode mode = chk.getExactlyonce() ? CheckpointingMode.EXACTLY_ONCE : CheckpointingMode.AT_LEAST_ONCE;

            env.enableCheckpointing(chk.getInterval() * 1000, mode);
            env.getCheckpointConfig().setCheckpointTimeout(chk.getTimeout().intValue() * 1000);
            env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
            env.getCheckpointConfig().setMinPauseBetweenCheckpoints(60000);
            env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
            env.getCheckpointConfig().setTolerableCheckpointFailureNumber(3);
        }

        env.setRestartStrategy(
                RestartStrategies.fixedDelayRestart(
                        sqlconfig.getAttempts().intValue(), Time.seconds(60)
                )
        );
    }

    private static List<String> resolveSqlLines(Sqljobconfig sqljobconfig) throws IOException {
        List<String> sqlLines = new ArrayList<>();
        if (!StringUtils.isNullOrWhitespaceOnly(sqljobconfig.getSqlfilepath())) {
            sqlLines = Files.readAllLines(Paths.get(sqljobconfig.getSqlfilepath()));
        }

        if (!StringUtils.isNullOrWhitespaceOnly(sqljobconfig.getSqlcontent())) {
            String sqlContent = new String(Base64.getDecoder().decode(sqljobconfig.getSqlcontent().getBytes()), "utf-8");
            // 按照回车切分
            sqlLines = Arrays.asList(sqlContent.split("\\r?\\n"));
        }

        if (sqlLines.size() == 0) {
            throw new RuntimeException("Sql Content is empty");
        }

        return sqlLines;
    }

    private static void callCommand(SqlCommandCall cmdCall, StreamTableEnvironment tEnv) {
        System.out.println(cmdCall.command);
        System.out.println(cmdCall.operands[0]);

        switch (cmdCall.command) {
            case SET:
                callSet(cmdCall, tEnv);
                break;
            case CREATE_TABLE:
            case INSERT_INTO:
            case CREATE_FUNCTION:
            case CREATE_VIEW:
                callSql(cmdCall, tEnv);
                break;
            default:
                throw new RuntimeException("Unsupported command: " + cmdCall.command);
        }
    }

    private static void callSet(SqlCommandCall cmdCall, StreamTableEnvironment tEnv) {
        String key = cmdCall.operands[0];
        String value = cmdCall.operands[1].trim();
        tEnv.getConfig().getConfiguration().setString(key, value);
    }

    private static void callSql(SqlCommandCall cmdCall, StreamTableEnvironment tEnv) {
        String ddl = cmdCall.operands[0];
        try {
            //tEnv.executeSql(ddl);
            tEnv.sqlUpdate(ddl);
        } catch (SqlParserException e) {
            throw new RuntimeException("SQL parse failed:\n" + ddl + "\n", e);
        }
    }

    public static void AddUserDefineFunctionToEnv(List<String> jarFullPaths, StreamExecutionEnvironment env) throws Exception {
        for (String jarPath : jarFullPaths) {
            loadJar(new URL(jarPath));
            System.out.println("==============>Load Jar Paths:" + jarPath);
        }

        if (!CollectionUtils.isEmpty(jarFullPaths)) {
            Field configuration = StreamExecutionEnvironment.class.getDeclaredField("configuration");
            configuration.setAccessible(true);
            Configuration o = (Configuration) configuration.get(env);

            Field confData = Configuration.class.getDeclaredField("confData");
            confData.setAccessible(true);
            Map<String, Object> temp = (Map<String, Object>) confData.get(o);
            // 一次性添加
            temp.put("pipeline.classpaths", jarFullPaths);
        }
    }

    //动态加载Jar
    public static void loadJar(URL jarUrl) {
        //从URLClassLoader类加载器中获取类的addURL方法
        Method method = null;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException | SecurityException e1) {
            e1.printStackTrace();
        }
        // 获取方法的访问权限
        boolean accessible = method.isAccessible();
        try {
            //修改访问权限为可写
            if (accessible == false) {
                method.setAccessible(true);
            }
            // 获取系统类加载器
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            //jar路径加入到系统url路径里
            method.invoke(classLoader, jarUrl);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.setAccessible(accessible);
        }
    }

}
