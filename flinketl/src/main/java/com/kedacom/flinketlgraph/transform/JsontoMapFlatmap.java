package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.accumulator.TransAccumulator;
import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Jsontomaptransformspec;
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

import java.util.Map;

public class JsontoMapFlatmap extends RichFlatMapFunction<Object, Object> implements CheckpointedFunction
{
    private static final long serialVersionUID = -7682536054964697925L;

    private Jsontomaptransformspec spec;
    private transient ObjectMapper mapper;
    private transient TransAccumulator acc;
    private transient ListState<TransmetricState> checkpointStates;
    private Graphnode joboperator;


    public JsontoMapFlatmap(Graphnode trans) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        spec = mapper.convertValue(trans.getElementconfig(), Jsontomaptransformspec.class);
        joboperator = trans;
    }

    @Override
    public void open(Configuration parameters) throws Exception
    {
        // if u do not understand this,do not bb
        this.mapper = new ObjectMapper();
        super.open(parameters);
        if (acc == null){
            acc = new TransAccumulator();
        }
        this.getRuntimeContext().addAccumulator(joboperator.getElementname()+":"+joboperator.getNodeid(), acc);
    }

    @Override
    public void flatMap(Object o, Collector<Object> collector) throws Exception
    {
        if (!(o instanceof String) && !(o instanceof byte[])){
            throw new InvalidTypesException("jsonflatmap inputtype is error");
        }
        acc.getLocalValue().processAddCount(1L);

        Map ret = null;
        try {
            if (o instanceof String)
            {
                ret = mapper.readValue((String)o, Map.class);
            }else{
                ret = mapper.readValue((byte[])o, Map.class);
            }
        }catch (Exception e){
            acc.getLocalValue().update(1L, e.getMessage());
        }
        // why collect must be outof trycatch, if u do not know , do not touch this
        if (ret != null){
            collector.collect(ret);
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
                new ListStateDescriptor<>("jsontomaptransform", TransmetricState.class);
        checkpointStates = context.getOperatorStateStore().getListState(descriptor);
        if (context.isRestored()){
            acc = CommonUtils.restoreTransmetricState2(checkpointStates);
        }
    }
}

