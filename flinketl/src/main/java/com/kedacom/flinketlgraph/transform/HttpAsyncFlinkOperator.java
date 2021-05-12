package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.AbstractFlinkOperator;
import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.*;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class HttpAsyncFlinkOperator extends AbstractFlinkOperator
{
    private ElementDescriptor desc = CommonUtils.createElementDesc(
            "flinktransform_httpasync",
            "httpasync transform",
            CommonUtils.Transform,
            CommonUtils.readfile("jsonfilesgraph/httpasynctransformspec.json"),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.httprequestbean),
                            "input 0",
                            "dataoinput"
                    )
            ),
            Arrays.asList(
                    new Elesingleportinfo(
                            0L,
                            Arrays.asList(CommonUtils.httpresponsebean),
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

    /*@Override
    public SingleOutputStreamOperator<Object> CreateTransform(DataStreamSource<Object> source, JobOperator transconfig) throws Exception
    {
        return createHttpAsync(source, transconfig);
    }

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(SingleOutputStreamOperator<Object> trans, JobOperator transconfig) throws Exception
    {
        return createHttpAsync(trans, transconfig);
    }*/

    @Override
    public SingleOutputStreamOperator<Object> CreateTransform(DataStream<Object> o, Graphnode transconfig, Graph graph) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        Httpasynctransformspec spec = mapper.convertValue(transconfig.getElementconfig(), Httpasynctransformspec.class);

        /*if (spec.getRetries()<0){
            spec.setRetries(1L);
        }
        long retries = spec.getRetries()+1;
        long onetime = spec.getTimeout().getConnecttimeout()+spec.getTimeout().getSockettimeout()+10;*/

        return AsyncDataStream.unorderedWait(o, new HttpAsyncFunction(transconfig),
                1800,
                TimeUnit.SECONDS, spec.getHttpconcurrency().intValue());

    }
}
