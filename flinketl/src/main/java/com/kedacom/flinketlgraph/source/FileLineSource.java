package com.kedacom.flinketlgraph.source;

import com.kedacom.flinketlgraph.json.Filelinessourcespec;
import com.kedacom.flinketlgraph.json.Graphnode;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

//RichSourceFunction
public class FileLineSource extends RichParallelSourceFunction<Object> implements CheckpointedFunction {
    private static final Logger mylogger = LoggerFactory.getLogger(FileLineSource.class);
    private Filelinessourcespec spec;
    private volatile boolean brunning = true;
    private FileReader fr = null;
    private BufferedReader bf = null;


    private Tuple2<String, Integer> xxx;
    private transient ListState<Tuple2<String, Integer>> checkpointedState;

    public FileLineSource(Graphnode trans) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        this.spec = mapper.convertValue(trans.getElementconfig(), Filelinessourcespec.class);
        String strfilepath = this.spec.getFilepath();
        if (strfilepath == null || strfilepath.length() == 0 || !new File(strfilepath).exists()) {
            throw new Exception("source file not exist");
        }
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        fr = new FileReader(xxx.f0);
        bf = new BufferedReader(fr);
    }


    @Override
    public void close() throws Exception {
        super.close();
        mylogger.info("source close called");
        if (bf != null) {
            bf.close();
            bf = null;
        }
        if (fr != null) {
            fr.close();
            fr = null;
        }
    }

    @Override
    public void run(SourceContext<Object> sourceContext) throws Exception {

        String str;
        while (brunning && (str = bf.readLine()) != null) {
            xxx.f1 += 1;
            sourceContext.collect(str);
            if (spec.getLinesinterval() > 0) {
                TimeUnit.MILLISECONDS.sleep(spec.getLinesinterval());
            }
            // test mylogger.info("version is 1.1.1.1");
        }
        while (brunning) {
            TimeUnit.MILLISECONDS.sleep(100);
            break;
        }
        mylogger.info("source run will exit");
    }

    @Override
    public void cancel() {
        brunning = false;
        mylogger.info("source cancel called");
    }

    @Override
    public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
        checkpointedState.clear();
        checkpointedState.add(xxx);
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<Tuple2<String, Integer>> descriptor =
                new ListStateDescriptor<>(
                        "sourcefilelines",
                        TypeInformation.of(new TypeHint<Tuple2<String, Integer>>() {
                        }));

        checkpointedState = context.getOperatorStateStore().getListState(descriptor);

        if (context.isRestored()) {
            for (Tuple2<String, Integer> element : checkpointedState.get()) {
                xxx = element;
                mylogger.info("init ok xxx is {}", xxx);
            }
        } else {
            xxx = new Tuple2<>(spec.getFilepath(), 0);
            mylogger.info("good this init error xxx is 0");
        }
    }
}
