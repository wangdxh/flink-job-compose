package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.*;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.util.OutputTag;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class SideoutFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createSideoutDesc(
            "flinktransform_sideout",
            "sideout transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/sideouttransformspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.object),
                            "input 0",
                            "dataoinput"
                    )
            ),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.object),
                            "output",
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
    public SingleOutputStreamOperator<Object> CreateTransform(DataStream<Object> trans,
                                                              Graphnode transconfig,
                                                              Graph graph,
                                                              Map<String, DataStream<Object>> outmap) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        Sideouttransformspec spec = mapper.convertValue(transconfig.getElementconfig(), Sideouttransformspec.class);
        if (spec.getSideouttags().size() == 0){
            throw new Exception("sideout tags size is 0, may be u do not need use sideout");
        }

        // 判断是否有重复
        spec.getSideouttags().stream().collect(Collectors.toMap(Sideouttag::getTagname, Sideouttag::getTagname));
        spec.getSideouttags().stream().collect(Collectors.toMap(Sideouttag::getOutputportindex, Sideouttag::getOutputportindex));

        SideoutProcess sideoutprocess = new SideoutProcess(transconfig, graph);
        SingleOutputStreamOperator<Object> sideout = trans.process(sideoutprocess)
                .returns(Types.GENERIC(Object.class));

        // 判断是否所有的输出port(对应 边的源端点)，都设置了tag
        for (Graphlink link : graph.getLinks()){
            if (link.getSourcenode().getNodeid().equals(transconfig.getNodeid())){
                boolean bfind = false;
                for(Sideouttag tag : spec.getSideouttags()){
                    if (tag.getOutputportindex().intValue() == link.getSourcenode().getPortindex()){
                        bfind = true;
                    }
                }
                if (false == bfind){
                    throw new Exception("sideout output index " + link.getSourcenode().getPortindex()
                            + " used but not set outputindex port tagname");
                }
            }
        }

        for (int i = 0; i < spec.getSideouttags().size(); i++) {
            DataStream<Object> out = null;
            Sideouttag mytag = spec.getSideouttags().get(i);
            if (mytag.getTagname().equals("default")){
                out = sideout;
            }else{
                OutputTag<Object> flinktag = new OutputTag<Object>(mytag.getTagname()){};
                out = sideout.getSideOutput(flinktag);
            }

            outmap.put(CommonUtils.getNodePortOutputstring(transconfig.getNodeid(), mytag.getOutputportindex()), out);
        }
        return sideout;
    }

}
