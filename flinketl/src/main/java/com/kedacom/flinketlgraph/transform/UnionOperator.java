package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.*;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UnionOperator extends AbstractFlinkOperator {

    ElementDescriptor desc = CommonUtils.createUnionDesc(
            "flinktransform_union",
            "union transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/unionspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.mapobject),
                            "input",
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
    public ElementDescriptor getoperatordesc() {
        return desc;
    }

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(final List<DataStream<Object>> translist, Graphnode transconfig, Graph graph) throws Exception {
        List<Graphnodeport> inputSelectedTypes = transconfig.getInputselectedtypes();

        if (transconfig.getInputselectedtypes().size() < 2) {
            throw new RuntimeException("union sources must more than 1");
        }

        Set<String> inputTypes = inputSelectedTypes.stream().map(node -> node.getPortselectedtype()).collect(Collectors.toSet());
        if (inputTypes.size() > 1) {
            throw new RuntimeException("union sources input type must be same");
        }


        DataStream<Object> firstStream = translist.get(0);
        DataStream<Object>[] unionDataStreams = new DataStream[translist.size() - 1];
        translist.subList(1, translist.size()).toArray(unionDataStreams);

        SingleOutputStreamOperator<Object> out = firstStream.union(unionDataStreams)
                .flatMap((FlatMapFunction<Object, Object>) (value, out1) -> out1.collect(value))
                .returns(Types.GENERIC(Object.class));
        return out;
    }


}
