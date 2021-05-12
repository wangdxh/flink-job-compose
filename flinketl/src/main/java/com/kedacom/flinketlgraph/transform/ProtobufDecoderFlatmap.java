package com.kedacom.flinketlgraph.transform;

import com.google.protobuf.Message;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.accumulator.TransAccumulator;
import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Protobufdecoderspec;
import com.kedacom.flinketlgraph.json.Topictoprotojavaclas;
import com.kedacom.flinketlgraph.source.KafkaFullData;
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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class ProtobufDecoderFlatmap extends RichFlatMapFunction<Object, Object> implements CheckpointedFunction
{
    private static final long serialVersionUID = -4798922470531535316L;
    private Protobufdecoderspec spec;

    private transient Map<String, Method> maptopictomethod;
    private transient Method onlyonetopicmethod;
    private transient TransAccumulator acc;
    private transient ListState<TransmetricState> checkpointStates;
    private Graphnode joboperator;


    public ProtobufDecoderFlatmap(Graphnode trans) throws Exception
    {
        joboperator = trans;
        ObjectMapper mapper = new ObjectMapper();
        spec = mapper.convertValue(trans.getElementconfig(), Protobufdecoderspec.class);
    }

    @Override
    public void open(Configuration parameters) throws Exception
    {
        this.maptopictomethod = new HashMap<>();
        if (acc==null){
            acc = new TransAccumulator();
        }
        getRuntimeContext().addAccumulator(joboperator.getElementname()+":"+joboperator.getNodeid(), acc);

        onlyonetopicmethod = null;
        for (Topictoprotojavaclas tp : spec.getTopictoprotojavaclass())
        {
            Class cls = Class.forName(tp.getProtojavaclassname());
            Method method = cls.getMethod("parseFrom", byte[].class);

            if (null == onlyonetopicmethod)
            {
                onlyonetopicmethod = method;
            }
            maptopictomethod.put(tp.getTopicname(), method);
        }
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void flatMap(Object o, Collector<Object> collector) throws Exception
    {
        byte[] data = null;
        Method method = null;
        if (o instanceof KafkaFullData)
        {
            KafkaFullData d = (KafkaFullData) o;
            data = d.getValue();
            method = maptopictomethod.get(d.getTopic());
        }
        else if (o instanceof byte[])
        {
            data = (byte[]) o;
            method = onlyonetopicmethod;
        }

        this.acc.getLocalValue().processAddCount(1L);

        Message pbMessage = null;
        try {
            if (null == method)
            {
                throw new InvalidTypesException("protobufdecoder flatmap java class can not find method");
            }
            pbMessage = (Message)method.invoke(null, data);
        }catch (Exception e){
            this.acc.getLocalValue().update(1L, e.getMessage());
        }

        // why collect must be outof trycatch, if u do not know , do not touch this
        if (pbMessage != null){
            collector.collect(pbMessage);
        }
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        checkpointStates.clear();
        checkpointStates.add(this.acc.getLocalValue());
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<TransmetricState> descriptor =
                new ListStateDescriptor<>("prototodecodertransform", TransmetricState.class);
        checkpointStates = context.getOperatorStateStore().getListState(descriptor);
        if (context.isRestored()){
            acc = CommonUtils.restoreTransmetricState2(checkpointStates);
        }
    }
}
