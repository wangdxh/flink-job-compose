package com.kedacom.flinketlgraphjob;

import com.kedacom.flinketlgraph.FlinkJobFactory;
import com.kedacom.flinketlgraph.json.Graph;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.StringUtils;

import java.io.File;
import java.util.Base64;


public class App {
    public static void main(String[] args) throws Exception {
        FlinkJobFactory.InitFlinkOperator();

        ParameterTool tool = ParameterTool.fromArgs(args);
        String filepath = tool.get("jsonfile");
        String jsonContent = tool.get("jsoncontent");
        ObjectMapper mapper = new ObjectMapper();
        Graph job = null;

        if (!StringUtils.isNullOrWhitespaceOnly(filepath)) {
            job = mapper.readValue(new File(filepath), Graph.class);
        } else if (!StringUtils.isNullOrWhitespaceOnly(jsonContent)) {
            jsonContent = new String(Base64.getDecoder().decode(jsonContent.getBytes()), "utf-8");
            job = mapper.readValue(jsonContent, Graph.class);
        } else {
            throw new Exception("jsonfile or jsoncontent parameters not set");
        }

        StreamExecutionEnvironment Env = FlinkJobFactory.CreateJob(job);
        Env.execute(job.getGraphconfig().getJobname());

        /*
        JobExecutionResult result =
        System.out.println("the result is over "+result.getJobID());

        String jsonresultpath = System.getenv(CommonUtils.FLINK_JOBRESULT_PATH);
        if (!StringUtils.isNullOrWhitespaceOnly(jsonresultpath)) {
            jsonresultpath += (jsonresultpath.endsWith(File.separator) ? "" : File.separator)
                    + result.getJobID() + ".json";
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonresultpath), result.getAllAccumulatorResults());
            System.out.println("write result to "+jsonresultpath);
        }else{
            System.out.println(result.getJobID() + "jobid get jobresultpath failed");
        }*/
    }
}
