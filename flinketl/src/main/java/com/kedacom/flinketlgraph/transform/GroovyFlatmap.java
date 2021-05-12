package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.GroovyInstanceUtils;
import com.kedacom.flinketlgraph.accumulator.TransAccumulator;
import com.kedacom.flinketlgraph.json.Graph;
import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Groovytransformspec;
import groovy.lang.GroovyObject;
import org.apache.flink.api.common.functions.InvalidTypesException;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class GroovyFlatmap extends RichFlatMapFunction<Object, Object>  implements CheckpointedFunction
{
    private static final long serialVersionUID = 3921539009821198563L;
    private static final Logger mylogger = LoggerFactory.getLogger(GroovyFlatmap.class);
    private GroovyObject instance;
    private String strgroovy = "";
    private boolean returnlisttoitem = false;
    private Groovytransformspec spec;
    private String inputtype;
    private String uid = "";
    private transient TransAccumulator acc;
    private transient ListState<TransmetricState> checkpointStates;
    private Graphnode joboperator;
    private String jobname;


    public GroovyFlatmap(Graphnode trans, Graph graphcfg) throws Exception {
        this.jobname = graphcfg.getGraphconfig().getJobname();
        this.uid = trans.getNodeid();
        if (uid.length()==0){
            throw new Exception("groovy uid length is 0");
        }
        mylogger.info("{} groovy transform uid is {}", trans.getElementname(), this.uid);
        this.inputtype = CommonUtils.getFirstInputtype(trans);//trans.getTypeinput();

        ObjectMapper mapper = new ObjectMapper();
        spec = mapper.convertValue(trans.getElementconfig(), Groovytransformspec.class);

        String strpath = spec.getGroovyfilepath();
        if (strpath != null && strpath.length() > 0){
            this.strgroovy = FileUtils.readFileUtf8(new File(strpath));
        }

        String strcontent = spec.getGroovyfilecontent();
        if (strcontent != null && strcontent.length() > 0){
            Base64.Decoder decoder = Base64.getDecoder();
            this.strgroovy = new String(decoder.decode(strcontent), "UTF-8");
        }
        if (this.strgroovy.length() == 0){
            throw new Exception("groovyflatmap groovy file no content");
        }
        mylogger.info("{} groovy transform groovy scripts :{}", trans.getElementname(), this.strgroovy);
        joboperator = trans;
    }

    @Override
    public void open(Configuration parameters) throws Exception
    {
        Class groovyclass = GroovyInstanceUtils.getGroovyClass(strgroovy, uid, jobname);
        instance = (GroovyObject)groovyclass.newInstance();

        try {
            returnlisttoitem = (boolean)instance.invokeMethod("returnlisttoitem", null);
            System.out.println("returnlisttoitem " + returnlisttoitem);
        }catch (Exception e){}

        super.open(parameters);
        if (acc==null){
            acc = new TransAccumulator();
        }
        this.getRuntimeContext().addAccumulator(joboperator.getElementname()+":"+joboperator.getNodeid(), acc);

    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void flatMap(Object o, Collector<Object> collector) throws Exception
    {
        if (inputtype.equals(CommonUtils.mapobject) && !(o instanceof Map))
        {
            throw new InvalidTypesException("groovyflatmap type is not mapobject");
        }
        acc.getLocalValue().processAddCount(1L);

        Object ret = null;
        try {
            ret = instance.invokeMethod("transform", o);
        }catch (Exception e){
            if (this.spec.getThrowgroovyexception()){
                throw  e;
            }
            acc.getLocalValue().update(1L, e.getMessage());
        }
        // why collect must be outof trycatch, if u do not know , do not touch this
        if (ret!=null){
            if (returnlisttoitem){
                List list = (List)ret;
                for (int inx = 0; inx < list.size(); inx++){
                    collector.collect(list.get(inx));
                }
            }else{
                collector.collect(ret);
            }
        }
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        checkpointStates.clear();
        checkpointStates.add(acc.getLocalValue());
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<TransmetricState> descriptor =
                new ListStateDescriptor<>("groovytransform", TransmetricState.class);
        checkpointStates = context.getOperatorStateStore().getListState(descriptor);
        if (context.isRestored()){
            acc = CommonUtils.restoreTransmetricState2(checkpointStates);
        }
    }
}

