package com.kedacom.flinketlgraph.transform;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.accumulator.TransAccumulator;
import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Protobufjsonnodespec;
import com.kedacom.flinketlgraph.json.Topictoprotoclas;
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
import org.apache.flink.util.FileUtils;

import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ProtobuftoMapFlatmap extends RichFlatMapFunction<Object, Object> implements CheckpointedFunction
{
    private static final long serialVersionUID = -4798922470531535316L;
    private Protobufjsonnodespec spec;
    private byte[] descbytes = new String("").getBytes();

    private transient Map<String, Descriptors.Descriptor> maptopictodesc;
    private transient Descriptors.Descriptor onlyonetopicdesc;
    private transient TransAccumulator acc;
    private transient ListState<TransmetricState> checkpointStates;
    private Graphnode joboperator;


    public ProtobuftoMapFlatmap(Graphnode trans) throws Exception
    {
        joboperator = trans;
        ObjectMapper mapper = new ObjectMapper();
        spec = mapper.convertValue(trans.getElementconfig(), Protobufjsonnodespec.class);

        String strpath = spec.getDescfilepath();
        if (strpath != null && strpath.length() > 0) {
            this.descbytes = FileUtils.readAllBytes(new File(strpath).toPath());
        }

        String strcontent = spec.getDescfilecontent();
        if (strcontent != null && strcontent.length() > 0){
            Base64.Decoder decoder = Base64.getDecoder();
            this.descbytes = decoder.decode(strcontent);
            // good idea
            spec.setDescfilecontent("");
        }

        if (this.descbytes.length == 0){
            throw new Exception("protobuf desc file length is 0");
        }
    }

    @Override
    public void open(Configuration parameters) throws Exception
    {
        this.maptopictodesc = new HashMap<>();
        if (acc==null){
            acc = new TransAccumulator();
        }
        getRuntimeContext().addAccumulator(joboperator.getElementname()+":"+joboperator.getNodeid(), acc);
        List<Descriptors.Descriptor> listDescriptors = CommonUtils.getDescriptorfromdescfile(this.descbytes);

        Map<String, Descriptors.Descriptor> mapprototodesc = listDescriptors.stream().collect(Collectors.toMap(
                Descriptors.Descriptor::getFullName, Function.identity()));

        onlyonetopicdesc = null;
        for (Topictoprotoclas tp : spec.getTopictoprotoclass())
        {
            Descriptors.Descriptor desc = mapprototodesc.get(tp.getProtoclassname());
            if (desc == null)
            {
                throw new InvalidTypesException("protobufflatmap topic " + tp.getTopicname() +
                        " no protoclass "+tp.getProtoclassname());
            }
            if (null == onlyonetopicdesc)
            {
                onlyonetopicdesc = desc;
            }
            maptopictodesc.put(tp.getTopicname(), desc);
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
        Descriptors.Descriptor pbDescritpor = null;
        if (o instanceof KafkaFullData)
        {
            KafkaFullData d = (KafkaFullData) o;
            data = d.getValue();
            pbDescritpor = maptopictodesc.get(d.getTopic());
        }
        else if (o instanceof byte[])
        {
            data = (byte[]) o;
            pbDescritpor = onlyonetopicdesc;
        }

        this.acc.getLocalValue().processAddCount(1L);

        Map<String, Object> result = null;
        try {
            if (null == pbDescritpor)
            {
                throw new InvalidTypesException("protobufflatmap protoclass can not find descriptor");
            }
            DynamicMessage.Builder pbBuilder = DynamicMessage.newBuilder(pbDescritpor);
            Message pbMessage = pbBuilder.mergeFrom(data).build();

            result = ProtobufMessageToMap.MessageToMap(pbMessage);
            result.put("_protoclass", pbDescritpor.getFullName());
        }catch (Exception e){
            this.acc.getLocalValue().update(1L, e.getMessage());
        }

        // why collect must be outof trycatch, if u do not know , do not touch this
        if (result != null){
            collector.collect(result);
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
                new ListStateDescriptor<>("prototomaptransform", TransmetricState.class);
        checkpointStates = context.getOperatorStateStore().getListState(descriptor);
        if (context.isRestored()){
            acc = CommonUtils.restoreTransmetricState2(checkpointStates);
        }
    }
}
