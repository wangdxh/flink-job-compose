package com.kedacom.flinketlgraph.minicluster;

import org.apache.flink.api.common.InvalidProgramException;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobgraph.ScheduleMode;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class MyLocalExecutionEnvironment extends StreamExecutionEnvironment {
    private static final Logger LOG = LoggerFactory.getLogger(MyLocalExecutionEnvironment.class);

    private final Configuration configuration;
    private String jobname;

    /**
     * Creates a new mini cluster stream environment that uses the default configuration.
     */
    public MyLocalExecutionEnvironment() {
        this(new Configuration());
    }

    /**
     * Creates a new mini cluster stream environment that configures its local executor with the given configuration.
     *
     * @param configuration The configuration used to configure the local executor.
     */
    public MyLocalExecutionEnvironment(@Nonnull Configuration configuration) {
        if (!ExecutionEnvironment.areExplicitEnvironmentsAllowed()) {
            throw new InvalidProgramException(
                    "The LocalStreamEnvironment cannot be used when submitting a program through a client, " +
                            "or running in a TestEnvironment context.");
        }
        this.configuration = configuration;
        setParallelism(1);
    }

    protected Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Executes the JobGraph of the on a mini cluster of ClusterUtil with a user
     * specified name.
     *
     * @return The result of the job execution, containing elapsed time and accumulators.
     */
    public JobExecutionResult execute(StreamGraph graph) throws Exception {
        JobGraph jobGraph =  graph.getJobGraph();
        //jobGraph.setAllowQueuedScheduling(true); need check again by wxh
        jobGraph.setScheduleMode(ScheduleMode.EAGER);

        Configuration configuration = new Configuration();
        configuration.addAll(jobGraph.getJobConfiguration());
        configuration.set(TaskManagerOptions.MANAGED_MEMORY_SIZE, new MemorySize(0));

        // add (and override) the settings with what the user defined
        configuration.addAll(this.configuration);

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

        MiniCluster miniCluster = new MiniCluster(cfg);
        try {
            miniCluster.start();
            configuration.setInteger(RestOptions.PORT, miniCluster.getRestAddress().get().getPort());

            return miniCluster.executeJobBlocking(jobGraph);
        }
        finally {
            transformations.clear();
            miniCluster.close();
        }
    }

    void clearTransformations(){
        this.transformations.clear();
    }

    JobGraph getJobGraph(String jobname){
        return this.getStreamGraph(jobname).getJobGraph();
    }

    /*public void setJobName(String jobname){
        this.jobname = jobname;
    }
    public String getJobName(){
        return this.jobname;
    }*/
}
