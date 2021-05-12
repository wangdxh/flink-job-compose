package com.kedacom.flinketlgraph;

import com.kedacom.flinketlgraph.json.*;
import com.kedacom.flinketlgraph.minicluster.MiddleCollectMap;
import com.kedacom.flinketlgraph.minicluster.MyLocalExecutionEnvironment;
import com.kedacom.flinketlgraph.minicluster.MyMiniCluster;
import com.kedacom.flinketlgraph.sink.*;
import com.kedacom.flinketlgraph.source.*;
import com.kedacom.flinketlgraph.transform.*;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FlinkJobFactory {
    private static final Logger LOG = LoggerFactory.getLogger(FlinkJobFactory.class);

    // 每个node的 输入输出索引，在能力级之下  输入输出类型在支持的类型内
    private static void GraphCheckLinkPortOk(Graphnodeport item, boolean bsource, Map<String, Graphnode> nodeidtonode) throws Exception {
        String selectedtype = item.getPortselectedtype();
        String nodeid = item.getNodeid();
        Long portinx = item.getPortindex();

        // 判断节点id，是否在节点列表中存在
        if (!nodeidtonode.containsKey(nodeid)) {
            throw new Exception(nodeid + "is not in the node list");
        }

        // 判断节点指向的元素类型， flink是否支持
        Graphnode node = nodeidtonode.get(nodeid);
        if (!operatormap.containsKey(node.getElementname())) {
            throw new Exception("there is no " + node.getElementname() + " element in the flink");
        }

        // source 意味着输出端口， dest指向的是节点的输入端口
        ElementDescriptor eledesc = operatormap.get(node.getElementname());
        Elementportslist portslist = bsource ? eledesc.getPortinfos().getOutputport() :
                eledesc.getPortinfos().getInputport();

        // -1代表动态端口个数，其他值表示固定数值，可以为0，0表示不支持输入或者输出
        if (portslist.getPortcount() != -1 && portinx >= portslist.getPortcount()) {
            throw new Exception("src port inx not in the port count range");
        }

        //System.out.println(nodeid);
        //System.out.println(node);
        System.out.println(item);

        // 判断选择的类型，是否在元素对应的端口上，支持的类型列表里面
        Elesingleportinfo portinfo = null;
        // 查找元素的portsinfo，需要根据对应的元素输出portcount来额外判断一下-1的情况
        if (portslist.getPortcount() == -1) {
            portinfo = portslist.getPortlist().get(0);
        } else {
            for (Elesingleportinfo p : portslist.getPortlist()) {
                if (p.getIndex().equals(portinx)) {
                    portinfo = p;
                    break;
                }
            }
        }
        // 支持的类型相同，或者支持 object
        if (!portinfo.getSupporttypes().contains(selectedtype)
                && !portinfo.getSupporttypes().contains(CommonUtils.object)) {
            throw new Exception("selected type is not in the support types ");
        }
    }

    private static void GraphCheckLinks(Graph graphcfg) throws Exception {
        Map<String, Graphnode> nodeidtonode = graphcfg.getNodes().stream().
                collect(Collectors.toMap(Graphnode::getNodeid, Function.identity()));

        // 连接的两个端点，对应的类型是一致的，object类型兼容所有的类型
        for (Graphlink link : graphcfg.getLinks()) {
            String srctype = link.getSourcenode().getPortselectedtype();
            String dsttype = link.getDestnode().getPortselectedtype();
            if (!srctype.equals(dsttype) && !srctype.equals(CommonUtils.object) && !dsttype.equals(CommonUtils.object)) {
                throw new Exception("types is not ok " + srctype + " != " + dsttype + " index is ");
            }

            GraphCheckLinkPortOk(link.getSourcenode(), true, nodeidtonode);
            GraphCheckLinkPortOk(link.getDestnode(), false, nodeidtonode);
        }

        //每个节点的输出端点只出现了一次， 输入端点只出现一次 这里判断一下是否有重复，重复抛出异常
        graphcfg.getLinks().stream().collect(
                Collectors.toMap(
                        p -> p.getSourcenode().getNodeid() + "#" + p.getSourcenode().getPortindex(),
                        Function.identity()));
        graphcfg.getLinks().stream().collect(
                Collectors.toMap(
                        p -> p.getDestnode().getNodeid() + "#" + p.getDestnode().getPortindex(),
                        Function.identity()));

    }

    // 对点进行校验
    private static void GraphCheckNodes(Graph graphcfg) throws Exception {
        // nodeid 不能重复
        Map<String, Graphnode> nodeidtonode = graphcfg.getNodes().stream().
                collect(Collectors.toMap(Graphnode::getNodeid, Function.identity()));

        for (Graphnode node : graphcfg.getNodes()) {
            // 校验node的配置 是否能通过jsonschema的校验
            CommonUtils.jsonSchemaValidator(
                    operatormap.get(node.getElementname()).getElementconfigschema(),
                    node.getElementconfig());

            //添加link 到 node的设置中，然后进行 list的排序
            String nodeid = node.getNodeid();
            if (nodeid.contains("#")) {
                throw new RuntimeException("nodeId can not contains #");
            }

            // 边的目的节点，指向的是 节点的输入；边的原始source节点，对应的是节点的输出
            for (Graphlink link : graphcfg.getLinks()) {
                if (link.getDestnode().getNodeid().equals(nodeid)) {
                    node.getInputselectedtypes().add(link.getDestnode());
                }
                if (link.getSourcenode().getNodeid().equals(nodeid)) {
                    node.getOutputselectedtypes().add(link.getSourcenode());
                }
            }
            // 元素允许的输入 输出的个数;
            // 等于-1 ，说明元素的输入输出的个数是动态的，为固定数值时，要满足固定数值的输入输出
            // source的 eleinput为0， sink的eleoutput为0，有错误输入时，都可以在这里判断出来

            Long eleinputportcount = operatormap.get(node.getElementname()).getPortinfos().getInputport().getPortcount();
            Long eleoutputportcount = operatormap.get(node.getElementname()).getPortinfos().getOutputport().getPortcount();

            if (eleinputportcount != -1 && node.getInputselectedtypes().size() != eleinputportcount) {
                throw new Exception(node.getElementname() + "#" + node.getNodeid() + "input port count not full");
            }

            if (eleoutputportcount != -1 && node.getOutputselectedtypes().size() != eleoutputportcount) {
                throw new Exception(node.getElementname() + "#" + node.getNodeid() + "output port count not full");
            }

            node.getInputselectedtypes().sort(Comparator.comparingLong(Graphnodeport::getPortindex));
            node.getOutputselectedtypes().sort(Comparator.comparingLong(Graphnodeport::getPortindex));
        }
    }

    private static void GraphCheckGraphOk(Graph graphcfg) throws Exception {
        // 校验graph 是否能通过jsonschema的校验

        String schemaContent = CommonUtils.readgraphschema();
        System.out.println(schemaContent);
        CommonUtils.jsonSchemaValidator(schemaContent, graphcfg);

        GraphCheckNodes(graphcfg);
        GraphCheckLinks(graphcfg);
    }

    private static void SetJobCheckpoint(StreamExecutionEnvironment env, Graph jobcfg) {
        Graphconfig graphconfig = jobcfg.getGraphconfig();
        Chkpointcfg chk = graphconfig.getChkpointcfg();

        if (chk.getEnable()) {
            // get checkpoint from OS's envi..ment,
            String checkpath = System.getenv(CommonUtils.FLINK_CHECKPOINT_PATH);
            if (!StringUtils.isNullOrWhitespaceOnly(checkpath)) {
                checkpath += (checkpath.endsWith(File.separator) ? "" : File.separator) + graphconfig.getJobname();
                env.setStateBackend(new FsStateBackend(checkpath));
                LOG.info("setstatebackend {} to job {}", checkpath, graphconfig.getJobname());
            }

            CheckpointingMode mode = chk.getExactlyonce() ? CheckpointingMode.EXACTLY_ONCE : CheckpointingMode.AT_LEAST_ONCE;

            env.enableCheckpointing(chk.getInterval() * 1000, mode);
            env.getCheckpointConfig().setCheckpointTimeout(chk.getTimeout().intValue() * 1000);
            env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
            env.getCheckpointConfig().setMinPauseBetweenCheckpoints(60000);
            env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
            //env.getCheckpointConfig().setPreferCheckpointForRecovery(false);
            env.getCheckpointConfig().setTolerableCheckpointFailureNumber(3);
        }
    }

    private static void SetJobParametersAndParallel(StreamExecutionEnvironment env, Graph graphcfg, boolean btest) {
        /*Configuration conf = new Configuration();
        conf.setString(CommonUtils.jobname, jobcfg.getJobname());
        conf.setInteger(CommonUtils.flinkstandalone, bstandalone ? 1 : 0);
        env.getConfig().setGlobalJobParameters(conf);*/

        if (btest) {
            env.setParallelism(1);
            env.setMaxParallelism(1);
            /*MyLocalExecutionEnvironment envlocal = (MyLocalExecutionEnvironment) env;
            envlocal.setJobName(graphcfg.getJobname());*/

        } else {
            Long jobparallel = graphcfg.getGraphconfig().getParallel();
            env.setParallelism(jobparallel.intValue());
        }
    }

    public static void SetOperatorUidAndPallael(DataStreamSink<Object> sink, Graphnode node) {
        sink.name(node.getElementname() + "-" + node.getNodeid()).uid(node.getNodeid());

        Integer parallel = CommonUtils.getflinkparallel(node);
        if (parallel != null && parallel > 0) {
            sink.setParallelism(parallel);
        }
    }

    public static void SetOperatorUidAndPallael(SingleOutputStreamOperator<Object> stream, Graphnode node) {
        stream.name(node.getElementname() + "-" + node.getNodeid()).uid(node.getNodeid());

        Integer parallel = CommonUtils.getflinkparallel(node);
        if (parallel != null && parallel > 0) {
            stream.setParallelism(parallel);
        }
    }


    // 获取连接到某个节点的输入port上的，所有前置的创建好的 datastream，通过输入边，反向追溯，
    public static List<DataStream<Object>> GraphGetAllPreviousDataStreamsFromOneNode(Graph graphcfg,
                                                                                     Graphnode node,
                                                                                     Map<String, DataStream<Object>> createdmap
    ) {
        List<DataStream<Object>> listout = new ArrayList<>();
        for (Graphnodeport inputport : node.getInputselectedtypes()) {
            // 根据节点的输入port信息，从边中，反推出该节点的前置节点列表
            // 边的source，就是点的output；边的dest，就是点的input
            for (Graphlink link : graphcfg.getLinks()) {
                if (link.getDestnode().getNodeid().equals(inputport.getNodeid())
                        && link.getDestnode().getPortindex().intValue() == inputport.getPortindex()) {
                    String strkey = link.getSourcenode().getNodeid() + "#" + link.getSourcenode().getPortindex();
                    if (createdmap.containsKey(strkey)) {
                        DataStream<Object> dataStream = createdmap.get(strkey);
                        listout.add(dataStream);
                    }
                }
            }
        }
        return listout;
    }

    // 根据边找到指向某个节点的所有前置节点列表
    public static Set<Graphnode> GraphGetPreviousNodesBeforeOneNode(Graph graphcfg, String nodeid, Set<Graphnode> set) {
        Map<String, Graphnode> nodeidtonode = graphcfg.getNodes().stream().
                collect(Collectors.toMap(Graphnode::getNodeid, Function.identity()));

        for (Graphlink link : graphcfg.getLinks()) {
            if (link.getDestnode().getNodeid().equals(nodeid)) {
                set.add(nodeidtonode.get(link.getSourcenode().getNodeid()));
            }
        }
        return set;
    }

    // 根据边找到某个节点指向的后续节点列表
    public static Set<Graphnode> GraphGetNodesAfterOneNode(Graph graphcfg, String nodeid, Set<Graphnode> set) {
        Map<String, Graphnode> nodeidtonode = graphcfg.getNodes().stream().
                collect(Collectors.toMap(Graphnode::getNodeid, Function.identity()));

        for (Graphlink link : graphcfg.getLinks()) {
            if (link.getSourcenode().getNodeid().equals(nodeid)) {
                set.add(nodeidtonode.get(link.getDestnode().getNodeid()));
            }
        }
        return set;
    }

    public static StreamExecutionEnvironment CreateJob(Graph graphcfg) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        return CreateCommonJob(env, graphcfg, false);
    }

    // job must has source, sink or transform is optional
    public static MyLocalExecutionEnvironment CreateTestJob(Graph graphcfg) throws Exception {
        MyLocalExecutionEnvironment env = MyMiniCluster.getLocalExecutionEnvironment();
        return (MyLocalExecutionEnvironment) CreateCommonJob(env, graphcfg, true);
    }

    private static StreamExecutionEnvironment CreateCommonJob(StreamExecutionEnvironment env,
                                                              Graph graphcfg,
                                                              boolean isTest) throws Exception {
        GraphCheckGraphOk(graphcfg);

        FlinkJobFactory.SetJobParametersAndParallel(env, graphcfg, false);
        FlinkJobFactory.SetJobCheckpoint(env, graphcfg);
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(graphcfg.getGraphconfig().getAttempts().intValue(), Time.seconds(60)));

        // create job 找到所有类型为 source的节点，就是所有待创建的flink算子
        List<Graphnode> waitforcreated = graphcfg.getNodes().stream().filter(p ->
                operatormap.get(p.getElementname()).getEnginecaptype().equals(CommonUtils.Source)
        ).collect(Collectors.toList());

        if (waitforcreated.size() == 0) {
            throw new Exception("no source element in the graph");
        }

        // 已经创建出来的DataStream算子，以 nodeid # portindex 作为key，portindex 指的是节点的输出的端口索引
        // 用于后面的节点，根据link边，来找到前置的已经创建好的datastream
        Map<String, DataStream<Object>> createdmap = new HashMap<>();
        int lastcreatemapnums = -1;

        for (; ; ) {
            if (waitforcreated.size() == 0) {
                break;
            }
            if (lastcreatemapnums == createdmap.size()) {
                throw new Exception("本轮没有创建 datastream，必定有异常发生");
            } else {
                lastcreatemapnums = createdmap.size();
            }

            // 下一轮中，可以创建的node节点，先缓存在set中
            Set<Graphnode> newwaitforcreated = new HashSet<>();

            Iterator<Graphnode> iter = waitforcreated.iterator();
            while (iter.hasNext()) {
                Graphnode node = iter.next();

                //元素允许的输入输出的个数,在元素的描述信息中指定 输入为0 是source；输出为0 是sink
                Long eleinputportcount = operatormap.get(node.getElementname()).getPortinfos().getInputport().getPortcount();
                Long eleoutputportcount = operatormap.get(node.getElementname()).getPortinfos().getOutputport().getPortcount();

                List<DataStream<Object>> previous = GraphGetAllPreviousDataStreamsFromOneNode(graphcfg, node, createdmap);
                if (previous.size() != node.getInputselectedtypes().size()) {
                    // 本节点的前置，尚未创建成功，导致本节点创建失败，尝试创建下一个节点
                    // source 的前置节点为0 可以通过校验
                    continue;
                }

                IFlinkOperator ins = classmap.get(node.getElementname());
                if (eleinputportcount == 0) {
                    // here is source
                    DataStreamSource<Object> src = ins.CreateSource(env, node, graphcfg);
                    SetOperatorUidAndPallael(src, node);

                    // source 都是一个output
                    Graphnodeport outport = node.getOutputselectedtypes().get(0);
                    createdmap.put(outport.getNodeid() + "#" + outport.getPortindex(), src);

                } else if (eleoutputportcount == 0) {
                    // here is sink
                    DataStreamSink<Object> sink = ins.CreateSink(previous.get(0), node, graphcfg);
                    SetOperatorUidAndPallael(sink, node);

                    // sink 就无所谓了，sink后面不需要继续连接
                    createdmap.put(node.getNodeid() + "#sink", previous.get(0));
                } else {
                    // 根据输出的算子个数不同，接口变化比较大
                    SingleOutputStreamOperator<Object> trans = null;
                    if (eleoutputportcount == 1) {
                        if (eleinputportcount == 1) {
                            trans = ins.CreateTransform(previous.get(0), node, graphcfg);
                        } else {
                            trans = ins.CreateTransform(previous, node, graphcfg);
                        }

                        Graphnodeport outport = node.getOutputselectedtypes().get(0);
                        createdmap.put(outport.getNodeid() + "#" + outport.getPortindex(), trans);

                    } else if (eleinputportcount == 1 && eleoutputportcount == -1) {
                        // 因为输出的个数是多个，不太好判断，所以这里放在算子内部去添加
                        // 可以在sideout内部判断，输出边和tagname的个数是否匹配
                        trans = ins.CreateTransform(previous.get(0), node, graphcfg, createdmap);
                    } else {
                        // 目前只支持 一进一出，一进多出，多进一出；
                        // 多进多出 目前不支持
                        throw new Exception("bad inputcount and outputcount");
                    }
                    SetOperatorUidAndPallael(trans, node);
                }

                //自己已经创建成功，从list中remove
                iter.remove();
                //创建成功，把后续要创建的节点，加入进来
                GraphGetNodesAfterOneNode(graphcfg, node.getNodeid(), newwaitforcreated);

                if (isTest && eleinputportcount != 0) {
                    for (Map.Entry<String, DataStream<Object>> createMapEntry : createdmap.entrySet()) {
                        if (createMapEntry.getKey().startsWith(node.getNodeid() + "#")) {
                            DataStream<Object> stream = createMapEntry.getValue();
                            SingleOutputStreamOperator<Object> collectStream = stream.map(new MiddleCollectMap(graphcfg.getGraphconfig().getJobname(), createMapEntry.getKey())).returns(Types.GENERIC(Object.class));
                            createMapEntry.setValue(collectStream);
                        }
                    }
                }
            }
            // 为什么要插入到开头？
            for (Graphnode item : newwaitforcreated) {
                // 节点既然要创建了，就把它从待创建的里面删除 在某种情况下 f 和 g 都在待创建的列表里
                // f是g的前置，f创建好之后，会把g加入到 下一轮待创建的set里，但是g已经在本轮待创建的里面了
                if (!waitforcreated.contains(item)) {
                    waitforcreated.add(0, item);
                }

                //检测回路，即将创建的nodeid，如果在已经创建的map中出现了，说明产生了回路
                for (String strkey : createdmap.keySet()) {
                    if (strkey.startsWith(item.getNodeid() + "#")) {
                        throw new Exception("there is loop in the graph");
                    }
                }
            }
        }
        System.out.println("***** operator key");
        createdmap.keySet().forEach(System.out::println);
        return env;

    }

    private static List<Class<? extends IFlinkOperator>> elementlist = new LinkedList<>();
    private static Map<String, ElementDescriptor> operatormap = new HashMap<>();
    private static List<ElementDescriptor> listdesc = new LinkedList<>();
    private static Map<String, IFlinkOperator> classmap = new HashMap<>();

    private static void CreateMap() throws Exception {
        for (Class<? extends IFlinkOperator> ope : elementlist) {

            IFlinkOperator ins = ope.newInstance();
            ElementDescriptor desc = ins.getoperatordesc();

            if (desc.getEnginecaptype().equals(CommonUtils.Source)
                    && CommonUtils.getportcount(desc, CommonUtils.input) != 0) {
                throw new Exception("bad source");
            }
            if (desc.getEnginecaptype().equals(CommonUtils.Sink)
                    && CommonUtils.getportcount(desc, CommonUtils.output) != 0) {
                throw new Exception("bad sink");
            }
            if (desc.getEnginecaptype().equals(CommonUtils.Transform)
                    && (CommonUtils.getportcount(desc, CommonUtils.input) == 0
                    || CommonUtils.getportcount(desc, CommonUtils.input) == 0)) {
                throw new Exception("bad transform");
            }

            operatormap.put(desc.getElementname(), desc);
            classmap.put(desc.getElementname(), ins);
            listdesc.add(desc);
        }
        ObjectMapper objmapper = new ObjectMapper();
        System.out.println(objmapper.writerWithDefaultPrettyPrinter().writeValueAsString(listdesc));
    }


    public static void InitFlinkOperator() throws Exception {
        if (elementlist.size() > 0) {
            LOG.warn("initflinkoperator ok do not need run again");
            return;
        }
        LOG.warn("initflinkoperator");

        elementlist.add(KafkaSourceFlinkOperator.class);
        elementlist.add(ProtobuftoMapFlinkOperator.class);
        elementlist.add(KafkaSinkFlinkOperator.class);
        elementlist.add(PrintSinkFlinkOperator.class);
        elementlist.add(DiscardSinkFlinkOperator.class);
        elementlist.add(GroovyFlinkOperator.class);
        elementlist.add(JsontoMapFlinkOperator.class);
        elementlist.add(FileLinesSourceFlinkOperator.class);
        elementlist.add(JdbcSourceFlinkOperator.class);
        elementlist.add(RedisSinkFlinkOperator.class);
        elementlist.add(JdbcSinkFlinkOperator.class);
        elementlist.add(HttpAsyncFlinkOperator.class);
        elementlist.add(ES7SinkFlinkOperator.class);
        elementlist.add(SideoutFlinkOperator.class);
        elementlist.add(RulerOperator.class);
        elementlist.add(RedisFlinkOperator.class);
        elementlist.add(FilesystemSourceFlinkOperator.class);
        elementlist.add(CsvtomapFlinkOperator.class);
        elementlist.add(JdbcSourceFlinkOperatorV2.class);
        elementlist.add(DelayFlinkOperator.class);
        elementlist.add(ConnectOperator.class);
        elementlist.add(WatermarkerAndTimestampOperator.class);
        elementlist.add(JoinOperator.class);
        elementlist.add(IntervalJoinOperator.class);
        elementlist.add(RedisSourceFlinkOperator.class);
        elementlist.add(ProtobufDecoderFlinkOperator.class);
        elementlist.add(UnionOperator.class);

        CreateMap();
    }

    public static List<ElementDescriptor> getOperatorDescList() {
        return listdesc;
    }
}
