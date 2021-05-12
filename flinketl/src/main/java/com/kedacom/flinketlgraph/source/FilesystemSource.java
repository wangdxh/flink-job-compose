package com.kedacom.flinketlgraph.source;

import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.accumulator.FileoffsetAccumulator;
import com.kedacom.flinketlgraph.json.Filesystemsourcespec;
import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Linefileconfig;
import com.kedacom.flinketlgraph.json.Structfileconfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class FilesystemSource extends RichParallelSourceFunction<Object> implements CheckpointedFunction {
    private static final Logger mylogger = LoggerFactory.getLogger(FilesystemSource.class);
    private Filesystemsourcespec spec;
    private volatile boolean brunning = true;
    private Pattern patt = null;

    private transient ArrayList<Fileoffset> toBeReadFiles = null;
    private transient List<Fileoffset> filesRestoreFromck = null;
    private transient ListState<Fileoffset> checkpointStates;
    private transient FileoffsetAccumulator acc;
    private Graphnode joboperator;

    public FilesystemSource(Graphnode trans) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        joboperator = trans;
        this.spec = mapper.convertValue(trans.getElementconfig(), Filesystemsourcespec.class);
        checkConfig();
        String regexString = this.spec.getFilterrule().replace("*", ".*")
                .replace("?", ".?");  // 获取文件名通配符规则转换成正则
        patt = Pattern.compile(regexString);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        toBeReadFiles = new ArrayList<>();

        if (filesRestoreFromck.size() == 0 ) {
            List<String> all = CommonUtils.listAllFiles(this.spec.getBasedirectory());
            if (all.size()>0){
                for (String filePath : all){
                    File file = new File(filePath);
                    Fileoffset fileoffset =
                            new Fileoffset(filePath, FileUtils.sizeOf(file), 0L, 0L, false, "");
                    if (this.isTargetFile(file.getName()) && !filesRestoreFromck.contains(fileoffset) ){
                        filesRestoreFromck.add(fileoffset);
                    }
                }
            }
        }
        if (filesRestoreFromck.size() == 0){
            throw new RuntimeException("no files to be read.");
        }
        Collections.sort(filesRestoreFromck);
        splitSourceFiles( );

        acc = new FileoffsetAccumulator(toBeReadFiles);
        getRuntimeContext().addAccumulator(joboperator.getElementname()+":"+joboperator.getNodeid(), acc);
    }

    @Override
    public void close() throws Exception {
        super.close();
        mylogger.info("source close called");
    }

    @Override
    public void run(SourceContext<Object> sourceContext) throws Exception {
        for (Fileoffset fileoffset : toBeReadFiles){
            if (!brunning){
                break;
            }
            if(fileoffset.getIsover()){
                continue;
            }
            mylogger.debug("start to process [%s]", fileoffset.getFilename());
            startRead(fileoffset, sourceContext);
        }
    }

    private void startRead(Fileoffset toBeReadFile, SourceContext<Object> sourceContext) throws IOException {
        try {
            File file = new File(toBeReadFile.getFilename());
            Filesystemsourcespec.Contenttype contenttype = this.spec.getContenttype();
            if (Filesystemsourcespec.Contenttype.LINES==contenttype){
                readFromLineFile(toBeReadFile, this.spec.getLinefileconfig(), sourceContext);
            }else if (Filesystemsourcespec.Contenttype.STRUCTS==contenttype){
                readFromStructFile(toBeReadFile, this.spec.getStructfileconfig(), sourceContext);
            }
            if (this.spec.getDelete()){
                file.delete();
            }
        } catch (Exception e) {
            String errMsg = e.getMessage();
            toBeReadFile.setErrormsg(errMsg);
            mylogger.error(errMsg);
        }finally {
            toBeReadFile.setIsover(true);
        }
    }

    private void readFromStructFile(Fileoffset toBeReadFile, Structfileconfig structfileconfig, SourceContext<Object> sourceContext) throws IOException {
        long fileoffset = toBeReadFile.getFileoffset();
        Long processednum = toBeReadFile.getProcessedNum();
        Structfileconfig.Endian endian = structfileconfig.getEndian();
        int lengthbytes = structfileconfig.getLengthbytes().intValue();
        byte[] lenByteAray = new byte[lengthbytes];
        try (BufferedRandomAccessFile braf =
                new BufferedRandomAccessFile(toBeReadFile.getFilename(), "r")) {
            braf.seek(fileoffset);
            while (this.brunning) {
                String message = null;
                int read = braf.read(lenByteAray, 0, lengthbytes);
                if (read == -1 || read != lengthbytes) {
                    message = String.format("[%d],fail to get msg len becase of eof or broken file, stop to process [%s]", read, toBeReadFile.getFilename());
                    throw new RuntimeException(message);
                }
                int len = 0;
                if (Structfileconfig.Endian.LITTLE==endian) {
                    len = CommonUtils.readSwappedInt(lengthbytes, lenByteAray).intValue();
                }else {
                    len = CommonUtils.byteArray2Long(lengthbytes, lenByteAray).intValue();
                }
                if (len > structfileconfig.getMsgsizelimit()){
                    message = String.format("msg size %d bytes, great than [msgsizelimit] %d bytes", len, structfileconfig.getMsgsizelimit());
                    throw new RuntimeException(message);
                }
                byte[] bytes = new byte[len];
                read = braf.read(bytes, 0, len);
                if (read == -1 || read != len) {
                    message = String.format("[%d], fail to get msg content becase of eof or broken file, stop to process [%s]", read, toBeReadFile.getFilename());
                    throw new RuntimeException(message);
                }
                sourceContext.collect(bytes);
                toBeReadFile.setFileoffset(braf.getFilePointer());
                toBeReadFile.setProcessedNum(++processednum);
            }
        }
    }

    private void readFromLineFile(Fileoffset toBeReadFile, Linefileconfig linefileconfig,
                                  SourceContext<Object> sourceContext) throws IOException {
        long fileOffset = toBeReadFile.getFileoffset();
        Long processednum = toBeReadFile.getProcessedNum();
        try (BufferedRandomAccessFile braf =
                     new BufferedRandomAccessFile(toBeReadFile.getFilename(), "r", linefileconfig.getEncoding())){
            Long skiplinenums = linefileconfig.getSkiplinenums();
            if (fileOffset==0 && skiplinenums>0){
                for (int i=0; i<skiplinenums; i++){
                    braf.readLine2();
                }
                toBeReadFile.setFileoffset(braf.getFilePointer());
            }else {
                braf.seek(fileOffset);
            }
            String line = null;
            while ((line=braf.readLine2())!=null && this.brunning){
                sourceContext.collect(line);
                toBeReadFile.setFileoffset(braf.getFilePointer());
                toBeReadFile.setProcessedNum(++processednum);
            }
        }
    }

    @Override
    public void cancel() {
        brunning = false;
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<Fileoffset> descriptor = new ListStateDescriptor<>("filesystemsource", Fileoffset.class);
        checkpointStates = context.getOperatorStateStore().getUnionListState(descriptor);
        filesRestoreFromck = new LinkedList<>();
        if (context.isRestored()){
            for (Fileoffset fileoffset : checkpointStates.get()){
                filesRestoreFromck.add(fileoffset);
            }
        }
    }

    @Override
    public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
        checkpointStates.clear();
        for (Fileoffset fileoffset : toBeReadFiles){
            checkpointStates.add(fileoffset);
        }
    }

    private boolean isTargetFile(String fileName) {
            return this.patt.matcher(fileName).matches();
    }

    private void splitSourceFiles() {
        int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();
        int numberOfParallelSubtasks = getRuntimeContext().getNumberOfParallelSubtasks();

        int averageLength = this.filesRestoreFromck.size() / numberOfParallelSubtasks;
        int mod = this.filesRestoreFromck.size() % numberOfParallelSubtasks;
        int start = indexOfThisSubtask * averageLength;
        int end = (indexOfThisSubtask+1) * averageLength;
        for (int i=start; i<end; i++){
            toBeReadFiles.add(this.filesRestoreFromck.get(i));
        }
        if (mod!=0 && indexOfThisSubtask<=mod-1){
            int tail = numberOfParallelSubtasks*averageLength+indexOfThisSubtask;
            toBeReadFiles.add(this.filesRestoreFromck.get(tail));
        }
    }

    private void checkConfig() throws Exception{
        if (StringUtils.isBlank(spec.getBasedirectory())){
            throw new RuntimeException("[ basedirectory ] is null");
        }
        File file = new File(spec.getBasedirectory());
        try {
            if (!file.exists() || !file.isDirectory()){ // isDirectory 方法可能会抛出非检查性权限异常
                throw new RuntimeException("[ basedirectory ] not exists");
            }
        } catch (SecurityException se) {
            throw se;
        }

        Filesystemsourcespec.Contenttype contenttype = spec.getContenttype();
        Linefileconfig linefileconfig = spec.getLinefileconfig();
        Structfileconfig structfileconfig = spec.getStructfileconfig();

        if (Filesystemsourcespec.Contenttype.LINES == contenttype){
            if (linefileconfig == null){
                throw new RuntimeException("[ contentformat ] is null");
            }else {
                String encoding = linefileconfig.getEncoding();
                if (!Charset.isSupported(encoding)){
                    throw new RuntimeException("[ encoding ] : "+encoding+"is unsupported. ");
                }
            }
        }else if (Filesystemsourcespec.Contenttype.STRUCTS == contenttype){
            if (structfileconfig == null){
                throw new RuntimeException("[ structfileconfig ] is null. ");
            }else {
                Long lengthbytes = structfileconfig.getLengthbytes();
                if (lengthbytes!=2 && lengthbytes!=4){
                    throw new RuntimeException("[ lengthbytes ]  error");
                }
            }
        }
    }

}
