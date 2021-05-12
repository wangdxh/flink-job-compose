package com.kedacom.flinketlgraph.sink;

import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Jdbcsinkspec;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JdbcSink extends RichSinkFunction<Object> implements CheckpointedFunction{
    private static final long serialVersionUID = 829936986178306610L;

    private static final Logger LOG = LoggerFactory.getLogger(JdbcSink.class);
    private Jdbcsinkspec spec;

    private transient PreparedStatement ps;
    private transient Connection connection;
    private volatile long batchnums;

    public JdbcSink(Graphnode trans)  throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        this.spec = mapper.convertValue(trans.getElementconfig(), Jdbcsinkspec.class);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        batchnums = 0;
        //bufferedlist = new LinkedList<>();

        String strurl = spec.getJdbcurl();
        if (strurl.startsWith("jdbc:mysql"))
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        else if (strurl.startsWith("jdbc:oracle"))
        {
            Class.forName("oracle.jdbc.OracleDriver");
        }
        else{
            throw new Exception("error database type "+strurl);
        }
        connection = DriverManager.getConnection(spec.getJdbcurl(), spec.getDbusername(), spec.getDbpassword());
        connection.setAutoCommit(false);
        ps = this.connection.prepareStatement(spec.getInsertsql());
    }

    @Override
    public void invoke(Object input, Context context) throws Exception {
        // for test the snapshotstate and the invoke  call time
        if (false){
            System.out.println("invoke data " + Thread.currentThread().getId());
        }

        if (! (input instanceof List)){
            throw new Exception("redis input data is not of list");
        }

        List list = (List)input;
        for (int inx = 0; inx < list.size(); inx++){
            ps.setObject(inx+1, list.get(inx));
        }
        ps.addBatch();
        batchnums++;

        if (batchnums >= spec.getBatchnums()){
            ps.executeBatch();
            connection.commit();
            batchnums = 0;
        }
    }

    public void close() throws Exception {
        super.close();
        //mytimer.cancel();

        if (ps != null) {
            ps.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
        // for test the snapshotstate and the invoke  call time
        if (false){
            System.out.println("snapshotState  will sleep 5 seconds " + Thread.currentThread().getId());
            TimeUnit.SECONDS.sleep(5);
        }

        if (batchnums > 0){
            ps.executeBatch();
            connection.commit();
            batchnums = 0;
        }
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        boolean b = context.isRestored();
    }
}
