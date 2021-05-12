package com.kedacom.flinketlgraph.source;

import com.kedacom.flinketlgraph.accumulator.JdbcSourceV2Accumulator;
import com.kedacom.flinketlgraph.accumulator.JdbcSourceV2State;
import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Jdbcsourcespecv2;
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
public class JdbcSourceV2 extends RichSourceFunction<Object> implements CheckpointedFunction {
    private static final Logger mylogger = LoggerFactory.getLogger(JdbcSourceV2.class);

    private Jdbcsourcespecv2 spec;
    private volatile boolean brunning = true;

    //private volatile Object uniquecol;
    //private volatile Long processednum;
    private volatile JdbcSourceV2State jdbcstate;
    private transient ListState<JdbcSourceV2State> checkpointedState;

    private PreparedStatement psselect;
    private PreparedStatement psselectfirst;
    private PreparedStatement pscount;

    private Connection connection;
    private List<String> listcolnames;

    private Graphnode joboperator;


    public JdbcSourceV2(Graphnode trans) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        this.spec = mapper.convertValue(trans.getElementconfig(), Jdbcsourcespecv2.class);
        this.joboperator = trans;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        String strurl = spec.getJdbcurl();
        if (strurl.startsWith("jdbc:mysql")) {
            Class.forName("com.mysql.jdbc.Driver");
        } else if (strurl.startsWith("jdbc:oracle")) {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //Class.forName("oracle.jdbc.OracleDriver");
        } else {
            throw new Exception("error database type " + strurl);
        }
        connection = DriverManager.getConnection(spec.getJdbcurl(), spec.getDbusername(), spec.getDbpassword());
        psselect = this.connection.prepareStatement(spec.getSelectsql());
        psselectfirst = this.connection.prepareStatement(spec.getFirstselectsql());
        pscount = this.connection.prepareStatement(spec.getCountsql());

        listcolnames = new LinkedList<>();

        getRuntimeContext().addAccumulator(joboperator.getElementname() + ":" + joboperator.getNodeid(),
                new JdbcSourceV2Accumulator(jdbcstate));
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (psselectfirst != null) {
            psselectfirst.close();
        }
        if (psselect != null) {
            psselect.close();
        }
        if (pscount != null) {
            pscount.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void run(SourceContext<Object> sourceContext) throws Exception {
        int x = 0;
        while (brunning) {
            ResultSet resultSetcount = pscount.executeQuery();
            if (resultSetcount.next()) {
                long count = resultSetcount.getLong(1);
                jdbcstate.setTotalnums(count);
            }

            PreparedStatement ps;
            if (jdbcstate.getUniquecolumn() == null) {
                ps = psselectfirst;
            } else {
                ps = psselect;
                ps.setObject(1, jdbcstate.getUniquecolumn());
            }

            ResultSet resultSet = ps.executeQuery();

            if (listcolnames.isEmpty()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    listcolnames.add(metaData.getColumnName(i));
                }
            }

            boolean hasdata = false;
            while (resultSet.next()) {
                hasdata = true;
                Map<Object, Object> map = new HashMap<>();
                for (String col : listcolnames) {
                    map.put(col, resultSet.getObject(col));
                }
                if (true) {
                    sourceContext.collect(map);
                    jdbcstate.addProcessedNum();
                } else {
                    TimeUnit.MILLISECONDS.sleep(10);
                    sourceContext.collect(map);
                    jdbcstate.addProcessedNum();
                    x++;
                    if (x == 5000) {
                        throw new Exception("test exception");
                    }
                }

                jdbcstate.setUniquecolumn(resultSet.getObject(spec.getUniquecolumn()));
            }
            if (!hasdata) {
                if (spec.getIncremental() == true) {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } else {
                    break;
                }
            }

            if (jdbcstate.getUniquecolumn() == null) {
                throw new Exception("uniquecol is null this cannot happen, select right unique");
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
        checkpointedState.add(jdbcstate);
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<JdbcSourceV2State> descriptor =
                new ListStateDescriptor<>(
                        "sourcemysql",
                        TypeInformation.of(new TypeHint<JdbcSourceV2State>() {
                        }));

        checkpointedState = context.getOperatorStateStore().getListState(descriptor);

        if (context.isRestored()) {
            for (JdbcSourceV2State element : checkpointedState.get()) {
                jdbcstate = element;
                mylogger.info("init ok uniquecol value is {}", jdbcstate);
            }
        } else {
            jdbcstate = new JdbcSourceV2State();
            mylogger.info("init error not restored uniquecol value is {}", jdbcstate);
        }
    }
}
