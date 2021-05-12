package com.kedacom.flinketlgraph.minicluster;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MiddleCollectMap extends RichMapFunction<Object, Object> {
    private static final long serialVersionUID = -8718772127345449990L;

    private String jobname;
    private String parentuid;
    public MiddleCollectMap(String jobname, String parentuid) {
        this.jobname = jobname;
        this.parentuid = parentuid;

        Map<String , List<Object>> jobmap = MiddleData.getJobData(jobname);
        jobmap.put(this.parentuid, new LinkedList<>());
    }

    @Override
    public void open(Configuration parameters) throws Exception {


        super.open(parameters);
    }

    @Override
    public Object map(Object o) throws Exception {
        MiddleData.addJobData(this.jobname, this.parentuid, o);
        return o;
    }

    @Override
    public void close() throws Exception {
        super.close();
    }


}
