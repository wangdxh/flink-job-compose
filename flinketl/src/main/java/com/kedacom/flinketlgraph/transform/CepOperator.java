package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.*;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;

import java.util.Arrays;

public class CepOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinktransform_cep",
            "cep transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/cepspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.mapobject),
                            "input 0",
                            "dataoinput"
                    )
            ),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.mapobject),
                            "output 0",
                            "dataoutput"
                    )
            )
    );

    @Override
    public ElementDescriptor getoperatordesc()
    {
        return desc;
    }

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(DataStream<Object> trans, Graphnode transconfig, Graph graph) throws Exception{
        StreamExecutionEnvironment env = trans.getExecutionEnvironment();

        ObjectMapper mapper = new ObjectMapper();
        Cepspec spec = mapper.convertValue(transconfig.getElementconfig(), Cepspec.class);
        Pattern<Object, ?> patternseq = null;
        OutputTag<Object> timeouttag = new OutputTag<Object>("timeouttag"){};
        SingleOutputStreamOperator<Object> stream = CEP
                .pattern(trans.keyBy(new MyKeySelector(spec.getKeyselector())), patternseq)
                .sideOutputLateData(timeouttag)
                .process(new CepPatternProcessFunction());

        // wait for extend
        stream.getSideOutput(timeouttag).print();

        return stream;
    }
}
