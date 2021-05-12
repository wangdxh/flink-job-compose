package com.kedacom.flinketlgraph.source;

import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Jdbcsourcespec;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//RichSourceFunction
public class JdbcSource extends RichSourceFunction<Object> implements CheckpointedFunction {
    private static final Logger mylogger = LoggerFactory.getLogger(JdbcSource.class);

    private Jdbcsourcespec spec;
    private volatile boolean brunning = true;
    private volatile Long offset;
    private transient ListState<Long> checkpointedState;

    private PreparedStatement ps;
    private Connection connection;
    private List<String> listcolnames;
    private int  questionmarknums = 0;

    public JdbcSource(Graphnode trans) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        this.spec = mapper.convertValue(trans.getElementconfig(), Jdbcsourcespec.class);
        this.questionmarknums = CommonUtils.appearNumber(spec.getSelectsql(), "?");
        if (this.questionmarknums == 0){
            throw new Exception("in jdbc source select sql is error , no question mark");
        }
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        String strurl = spec.getJdbcurl();
        if (strurl.startsWith("jdbc:mysql"))
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        else if (strurl.startsWith("jdbc:oracle"))
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //Class.forName("oracle.jdbc.OracleDriver");
        }
        else{
            throw new Exception("error database type "+strurl);
        }
        connection = DriverManager.getConnection(spec.getJdbcurl(), spec.getDbusername(), spec.getDbpassword());
        ps = this.connection.prepareStatement(spec.getSelectsql());
        listcolnames = new LinkedList<>();
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (ps != null) {
            ps.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void run(SourceContext<Object> sourceContext) throws Exception {
        while (brunning){
            for (int i = 0; i < questionmarknums; i++) {
                ps.setLong(i+1, offset);
            }

            ResultSet resultSet = ps.executeQuery();

            if (listcolnames.isEmpty()){
                ResultSetMetaData metaData = resultSet.getMetaData();
                for( int i=1; i <= metaData.getColumnCount(); i++){
                    listcolnames.add(metaData.getColumnName(i));
                }
            }

            boolean hasdata = false;
            while (resultSet.next()){
                hasdata = true;
                Map<Object, Object> map = new HashMap<>();
                for (String col : listcolnames) {
                    map.put(col, resultSet.getObject(col));
                }
                sourceContext.collect(map);
                offset++;
            }
            if (!hasdata){
                TimeUnit.MILLISECONDS.sleep(1000);
            }else{
                mylogger.info("now offset in jdbc is {}", offset);
            }
        }
    }

    @Override
    public void cancel() {
        brunning = false;
    }

    @Override
    public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
        checkpointedState.clear();
        checkpointedState.add(offset);
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<Long> descriptor =
                new ListStateDescriptor<>(
                        "sourcemysql",
                        TypeInformation.of(new TypeHint<Long>() {
                        }));

        checkpointedState = context.getOperatorStateStore().getListState(descriptor);

        if (context.isRestored()) {
            for (Long element : checkpointedState.get()) {
                offset = element;
                mylogger.info("init ok xxx is {}", offset);
            }
        } else {
            offset = spec.getStartoffset();
            mylogger.info("good init error xxx is {}", offset);
        }
    }
}
