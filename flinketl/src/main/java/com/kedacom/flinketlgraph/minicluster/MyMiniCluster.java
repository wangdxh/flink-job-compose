package com.kedacom.flinketlgraph.minicluster;

import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.FlinkJobFactory;
import com.kedacom.flinketlgraph.json.Graph;
import com.kedacom.flinketlgraph.json.Graphconfig;
import com.kedacom.flinketlgraph.json.Jobtestrequest;
import com.kedacom.flinketlgraph.json.Jobtestresult;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.JobStatus;
import org.apache.flink.api.common.JobSubmissionResult;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.runtime.client.JobExecutionException;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobmaster.JobResult;
import org.apache.flink.runtime.messages.Acknowledge;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MyMiniCluster {
    private static final Logger LOG = LoggerFactory.getLogger(MyMiniCluster.class);
    private static MiniCluster miniCluster = null;

    public static void initCluster() throws Exception {
        if (miniCluster != null) {
            return;
        }
        Configuration configuration = new Configuration();
        configuration.set(TaskManagerOptions.MANAGED_MEMORY_SIZE, new MemorySize(0));

        if (!configuration.contains(RestOptions.BIND_PORT)) {
            configuration.setString(RestOptions.BIND_PORT, "0");
        }

        int numSlotsPerTaskManager = 1;//configuration.getInteger(TaskManagerOptions.NUM_TASK_SLOTS, jobGraph.getMaximumParallelism());

        MiniClusterConfiguration cfg = new MiniClusterConfiguration.Builder()
                .setConfiguration(configuration)
                .setNumSlotsPerTaskManager(numSlotsPerTaskManager)
                .build();

        if (LOG.isInfoEnabled()) {
            LOG.info("Running job on local embedded Flink mini cluster");
        }

        miniCluster = new MiniCluster(cfg);
        miniCluster.start();
    }

    public static void stopCluster() throws Exception {
        if (miniCluster!=null){
            miniCluster.close();
            miniCluster = null;
        }
    }

    public static MyLocalExecutionEnvironment getLocalExecutionEnvironment() throws ExecutionException, InterruptedException {
        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, miniCluster.getRestAddress().get().getPort());

        final MyLocalExecutionEnvironment env = new MyLocalExecutionEnvironment(configuration);
        env.setMaxParallelism(1);
        return env;
    }

    public static JobID submitJob(JobGraph graph) throws ExecutionException, InterruptedException {
        CompletableFuture<JobSubmissionResult> jobfuture = miniCluster.submitJob(graph);
        JobSubmissionResult result = jobfuture.get();
        JobID jid = result.getJobID();
        return jid;
    }

    public static void cancelJob(JobID jid) {// throws ExecutionException, InterruptedException {
        try {
            CompletableFuture<Acknowledge> fut2 = miniCluster.cancelJob(jid);
            fut2.get();
        } catch (Exception e) {
            // do nothing just cancel
        }
    }

    public static boolean getJobStatus(JobID jid) throws Exception {
        CompletableFuture<JobStatus> futureStatus = miniCluster.getJobStatus(jid);
        JobStatus status = futureStatus.get();
        if (status.isTerminalState()) {
            JobResult jobResult = miniCluster.requestJobResult(jid).get();
            //try {
            JobExecutionResult result2 = jobResult.toJobExecutionResult(Thread.currentThread().getContextClassLoader());
            //} catch (ClassNotFoundException | IOException | JobExecutionException e) {}
            return true;
        }
        return false;
    }

    public static Jobtestresult testJob(Jobtestrequest req) {
        Jobtestresult testres = new Jobtestresult();
        Graph job = null;
        MyLocalExecutionEnvironment env = null;
        Graphconfig graphconfig = null;
        try
        {
            String filepath = req.getJsonfile();
            if (StringUtils.isNullOrWhitespaceOnly(filepath) && req.getJobconfig() == null){
                throw new Exception("one of filepath or jobconfig must be set");
            }
            if (req.getJobconfig() != null){
                job = req.getJobconfig();
            }else{
                ObjectMapper mapper = new ObjectMapper();
                job = mapper.readValue(new File(filepath), Graph.class);
            }

            int timeoutseconds = req.getTimeout().intValue();
            int needdatanums = req.getDatanums().intValue();

            MiddleData.initJobData(job.getGraphconfig().getJobname(), needdatanums);
            env = FlinkJobFactory.CreateTestJob(job);

            graphconfig = job.getGraphconfig();
            JobID jid = MyMiniCluster.submitJob(env.getJobGraph(graphconfig.getJobname()));
            System.out.println("job id is " + jid);

            int loopcount = 0;
            boolean bterminated;
            boolean bhasdata = false;
            boolean bdataenough = false;
            while (true) {
                TimeUnit.MILLISECONDS.sleep(100);
                bterminated = MyMiniCluster.getJobStatus(jid);
                if (bterminated) {
                    break;
                }
                loopcount++;
                if (loopcount > timeoutseconds * 10) {
                    break;
                }

                Map<String, List<Object>> data = MiddleData.getJobData(graphconfig.getJobname());
                //System.out.println("totalsize :"+data.size());
                bdataenough = true;

                for (List<Object> item : data.values()) {
                    if (item.size() > 0) {
                        bhasdata = true;
                    }
                    //System.out.println(item.size());
                    if (item.size() < needdatanums) {
                        bdataenough = false;
                        break;
                    }
                }
                if (bdataenough) {
                    break;
                }
            }
            if (!bterminated) {
                System.out.println("cancel job id " + jid);
                MyMiniCluster.cancelJob(jid);
                if (!bdataenough && !bhasdata) {
                    testres.setCode(-2L);
                    testres.setMessage(timeoutseconds + " seconds time out and not get any data");
                    return testres;
                }
                Map<String, List<Object>> data = MiddleData.getJobData(graphconfig.getJobname());
                System.out.println("------------------------------------------------");
                data.forEach((k, v) -> {
                    System.out.println(k + "  " + v.size());
                    v.forEach(i -> {
                        System.out.println(i.getClass() + "  " + i);
                    });
                });
            }
            testres.setCode(0L);
            testres.setMessage("OK");
            testres.setData(MiddleData.getJobData(graphconfig.getJobname()));
        } catch (Exception e) {
            Throwable exp = e;
            if (e instanceof JobExecutionException) {
                exp = e.getCause();
            }
            testres.setCode(-1L);
            testres.setMessage(exp.getMessage());
            testres.setExceptionstack(CommonUtils.getStackFromThrowable(exp));
        } finally {
            if (job!=null){
                MiddleData.clearJobData(graphconfig.getJobname());
            }
            if (env != null){
                env.clearTransformations();
            }
        }
        return testres;
    }
}
