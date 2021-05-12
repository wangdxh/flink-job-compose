package com.kedacom.flinketlgraph;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.kedacom.flinketlgraph.accumulator.TransAccumulator;
import com.kedacom.flinketlgraph.json.*;
import com.kedacom.flinketlgraph.transform.TransmetricState;
import com.kedacom.flinketlgraph.minicluster.MyLocalExecutionEnvironment;
import org.apache.commons.io.EndianUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.http.HttpHost;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommonUtils {
    public static String readfile(String strpath) {
        String string = "";
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(strpath);
        byte[] buff;

        try {

            buff = new byte[inputStream.available()];
            int len;
            while ((len = inputStream.read(buff)) != -1) {
                string += new String(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String readjsonschemafile(String strpath, String strrefsfile) {
        // ^\{[\s*"\w+":"\w+",*\s*]+\}$ json stupid regular!!!!
        String strcontent = readfile(strpath);
        int x = strcontent.indexOf(strrefsfile.substring(strrefsfile.indexOf("/") + 1));
        if (x != -1) {
            int start = strcontent.lastIndexOf("{", x);
            int end = strcontent.indexOf("}", x);
            String strfind = strcontent.substring(start, end + 1);

            String strref = readfile(strrefsfile);
            String out = strcontent.replace(strfind, strref);
            return out;
        }
        return strcontent;
    }

    public static String replacejsonschemastring(String strpathcontent, String strrefsfilename, String reffilecontent) {
        String strcontent = strpathcontent;

        int x = strcontent.indexOf(strrefsfilename);
        if (x != -1) {
            int start = strcontent.lastIndexOf("{", x);
            int end = strcontent.indexOf("}", x);
            String strfind = strcontent.substring(start, end + 1);
            //replace 是replace all
            strcontent = strcontent.replace(strfind, reffilecontent);
        }
        return strcontent;
    }

    public static String readgraphschema(){
        String graphcontent = readfile("jsonfilesgraph/graph.json");
        String nodecontent = readfile("jsonfilesgraph/graphnode.json");
        String linkcontent = readfile("jsonfilesgraph/graphlink.json");
        String portcontent = readfile("jsonfilesgraph/graphnodeport.json");

        nodecontent = replacejsonschemastring(nodecontent, "graphnodeport.json", portcontent);
        linkcontent = replacejsonschemastring(linkcontent, "graphnodeport.json", portcontent);
        graphcontent = replacejsonschemastring(graphcontent, "graphnode.json", nodecontent);
        graphcontent = replacejsonschemastring(graphcontent, "graphlink.json", linkcontent);

        return graphcontent;
    }

    public static String getflinkuid(Graphnode job) {
        String name = job.getElementname();
        String uid = job.getNodeid();
        return uid;
    }

    public static Integer getflinkparallel(Graphnode job) {
        Map elementConfig = (Map) job.getElementconfig();
        return (Integer) elementConfig.get("parallel");
    }

    public static boolean isMyLocalExecutionEnvironment(StreamExecutionEnvironment env){
        return env instanceof MyLocalExecutionEnvironment;
    }

    public static String readfilefromdisk(String strpath) {
        String string = "";
        try {
            InputStream inputStream = new FileInputStream(strpath);
            byte[] buff;
            buff = new byte[inputStream.available()];
            int len;
            while ((len = inputStream.read(buff)) != -1) {
                string += new String(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static final String Source = "source";
    public static final String Transform = "transform";
    public static final String Sink = "sink";

    public static final String bytearray = "bytearray";
    public static final String mapobject = "mapobject";
    public static final String protobufmessage = "protobufmessage";
    public static final String listobject = "listobject";
    public static final String kafkafulldata = "kafkafulldata";
    public static final String filefulldata = "filefulldata";
    public static final String stringtype = "string";
    public static final String object = "object";
    public static final String esindexbean = "esindexbean";
    public static final String httprequestbean = "httprequestbean";
    public static final String httpresponsebean = "httpresponsebean";
    public static final String joinresult = "joinresult";
    public static final String RedisTransInputBean = "RedisTransInputBean";
    public static final String RedisTransResultBean = "RedisTransResultBean";


    //public static final String flinkstandalone = "flinkstandalone";
    //public static final String jobname = "jobname";

    public static final String FLINK_CHECKPOINT_PATH = "FLINK_CHECKPOINT_PATH";

    public static final String flink = "flink";
    public static final String algorithm = "algorithm";


    public static List<Descriptors.Descriptor> getDescriptorfromdescfile(byte[] descdata) {
        List<Descriptors.Descriptor> listpbDescritpor = new ArrayList<>();

        //desc 是proto文件的集合，尤其有依赖时,多个proto文件的定义在一个desc文件里面。
        DescriptorProtos.FileDescriptorSet descriptorSet = null;
        try {
            descriptorSet = DescriptorProtos.FileDescriptorSet.parseFrom(descdata);
            //.parseFrom(new FileInputStream(strpath));
        } catch (IOException e) {
            e.printStackTrace();
            return listpbDescritpor;
        }

        ArrayList<Descriptors.FileDescriptor> filedesclist = new ArrayList<>();

        //解析每个proto文件的描述信息，有include时，需要把依赖的文件填进来
        for (DescriptorProtos.FileDescriptorProto fdp : descriptorSet.getFileList()) {
            //解析出来的结构体描述数组，用于结构体依赖
            Descriptors.FileDescriptor[] filedescarray = new Descriptors.FileDescriptor[filedesclist.size()];
            filedesclist.toArray(filedescarray);

            //从单个文件中解析文件描述
            Descriptors.FileDescriptor fileDescriptor = null;
            try {
                fileDescriptor = Descriptors.FileDescriptor.buildFrom(fdp, filedescarray);
            } catch (Descriptors.DescriptorValidationException e) {
                e.printStackTrace();
                return null;
            }
            filedesclist.add(fileDescriptor);

            //一个proto文件里面可能会定义多个结构体，这里遍历一下
            for (Descriptors.Descriptor descriptor : fileDescriptor.getMessageTypes()) {
                //得到每个结构体的详细信息
                String className = fdp.getOptions().getJavaPackage() + "."
                        + fdp.getOptions().getJavaOuterClassname() + "$"
                        + descriptor.getName();
                System.out.println(descriptor.getFullName() + " -> " + className);
                for (Descriptors.FieldDescriptor field : descriptor.getFields()) {
                    System.out.println("\t" + field.getName() + "-----" + field.getType().toString());
                }
                listpbDescritpor.add(descriptor);
            }
        }
        return listpbDescritpor;
    }

    public static String getStackFromThrowable(Throwable except) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        except.printStackTrace(pw);
        return sw.toString();
    }

    public static List<HttpHost> getClusterAddresses(String hosts) throws MalformedURLException {
        String[] hostList = hosts.split(",");
        List<HttpHost> addresses = new ArrayList<>();
        for (String host : hostList) {
            if (host.startsWith("http")) {
                URL url = new URL(host);
                addresses.add(new HttpHost(url.getHost(), url.getPort()));
            } else {
                String[] parts = host.split(":", 2);
                if (parts.length > 1) {
                    addresses.add(new HttpHost(parts[0], Integer.parseInt(parts[1])));
                } else {
                    throw new MalformedURLException("invalid elasticsearch hosts format");
                }
            }
        }
        return addresses;
    }


    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
    }

    // 递归获取指定目录下面所有能访问的文件
    public static List<String> listAllFiles(String topDirectory){
        List<String> allFiles = new LinkedList<>();
        directoryRecursiveTravel(topDirectory, allFiles);
        return allFiles;
    }
    private static void directoryRecursiveTravel(String topDirectory, List<String> allFiles) {
        File directory = new File(topDirectory);
        // 对于没有权限和不存在的目录,listFiles 返回null，
        File[] files = directory.listFiles();
        if (null != files) {
            for (File file : files) {
                if (!file.isDirectory()){
                    allFiles.add(file.getAbsolutePath());
                }else {
                    directoryRecursiveTravel(file.getAbsolutePath(), allFiles);
                }
            }
        }
    }

    /**
     * 把小端存储的数值字节数组，直接转换成整型数值 ( jdk 平台short[2byte], int[4byte], long[8byte]) jvm默认大端存储
     * @param len 数值字节数组长度
     * @param lenByteAray 数组字节数组
     * @return
     */
    public static Long readSwappedInt(int len, byte[] lenByteAray) {
        long value = 0;
        if (len == 2) {
            value = EndianUtils.readSwappedShort(lenByteAray, 0);
        }
        else if (len == 4) {
            value = EndianUtils.readSwappedInteger(lenByteAray, 0);
        }
        else if (len == 8){
            value = EndianUtils.readSwappedInteger(lenByteAray, 0);
        }else {
            throw new RuntimeException("Out of Long precision");
        }
        return Long.valueOf(value);
    }

    /**
     * 把大端 byte 数组转成int
     * @param len 有效字节数
     * @param bytes
     * @return
     */
    public static Long byteArray2Long(int len, byte[] bytes) {
        Long value = 0L;
        // 由高位到低位
        for (int i = 0; i < len; i++) {
            int shift = (len - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public static TransAccumulator restoreTransmetricState2(ListState<TransmetricState> checkpointStates) throws Exception {
        TransAccumulator acc = new TransAccumulator();
        for (TransmetricState elem : checkpointStates.get()) {
            acc.add(elem);
        }
        return acc;
    }

    public static TransmetricState restoreTransmetricState(ListState<TransmetricState> checkpointStates) throws Exception {
        TransmetricState state = new TransmetricState();
        for (TransmetricState elem : checkpointStates.get()) {
            state.AddTransmetricState(elem);
        }
        return state;
    }

    public static void jsonSchemaValidator(Object jsonSchemaContent, Object obj) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String strElementSchema = mapper.writeValueAsString(jsonSchemaContent);
        jsonSchemaValidator(strElementSchema, obj);
    }

    public static void jsonSchemaValidator(String jsonSchemaConent, Object obj) throws Exception{
        jsonSchemaConent = jsonSchemaConent.replace("\"any\"", "\"object\"");
        if (StringUtils.isBlank(jsonSchemaConent)){
            return;
        }
        try {
            JSONObject rawSchema = new JSONObject(jsonSchemaConent);
            Schema schema = SchemaLoader.load(rawSchema);

            ObjectMapper objectMapper = new ObjectMapper();
            String string = objectMapper.writeValueAsString(obj);
            schema.validate(new JSONObject(string));
        } catch (ValidationException e) {
            ObjectMapper objectMapper = new ObjectMapper();
            Object errMsgObj = objectMapper.readValue(e.toJSON().toString(), Object.class);
            throw new Exception(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errMsgObj));
        }
    }

    public static ElementDescriptor createElementDesc(String name, String desc, String captype,
                                                      String configschema,
                                                      List<Elesingleportinfo> input,
                                                      List<Elesingleportinfo> output){
        ElementDescriptor eledesc = new ElementDescriptor(
                name,
                desc,
                CommonUtils.algorithm,
                CommonUtils.flink,
                captype,
                configschema,
                new Portinfos(new Elementportslist(), new Elementportslist())
        );

        ObjectMapper mapper = new ObjectMapper();
        if (!StringUtils.isEmpty(configschema)) {
            try {
                Object schemaObj = mapper.readValue(configschema, Object.class);
                eledesc.setElementconfigschema(schemaObj);
            } catch (IOException e) {
                throw new RuntimeException("node : json schema string can not convert to json object");
            }
        } else {
            throw new RuntimeException("node : json schema can not null");
        }

        Elementportslist eleinput = eledesc.getPortinfos().getInputport();
        eleinput.setPortcount((long) input.size());
        eleinput.setPortlist(input);

        Elementportslist eleoutput = eledesc.getPortinfos().getOutputport();
        eleoutput.setPortcount((long)output.size());
        eleoutput.setPortlist(output);
        return eledesc;
    }

    public static ElementDescriptor createSideoutDesc(String name, String desc, String captype,
                                                      String configschema,
                                                      List<Elesingleportinfo> input,
                                                      List<Elesingleportinfo> output){
        ElementDescriptor eledesc = createElementDesc(name, desc, captype, configschema, input, output);
        eledesc.getPortinfos().getOutputport().setPortcount(-1L);
        return eledesc;
    }

    public static ElementDescriptor createUnionDesc(String name, String desc, String captype,
                                                     String configschema,
                                                     List<Elesingleportinfo> input,
                                                     List<Elesingleportinfo> output) {
        ElementDescriptor eledesc = createElementDesc(name, desc, captype, configschema, input, output);
        eledesc.getPortinfos().getInputport().setPortcount(-1L);
        return eledesc;
    }

    public static String getFirstInputtype(Graphnode node) throws Exception{
        if (node.getInputselectedtypes().size() < 1){
            throw new Exception("input selected types is 0");
        }
        return node.getInputselectedtypes().get(0).getPortselectedtype();
    }

    public static String getFirstOutputtype(Graphnode node) throws Exception{
        if (node.getOutputselectedtypes().size() < 1){
            throw new Exception("output selected types is 0");
        }
        return node.getOutputselectedtypes().get(0).getPortselectedtype();
    }

    public static String getNodePortOutputstring(String nodeid, Long index){
        return nodeid + "#" + index;
    }

    public static final String input = "input";
    public static final String output = "output";
    public static Long getportcount(ElementDescriptor eledesc, String type){
        if (type.equals(input)){
            return eledesc.getPortinfos().getInputport().getPortcount();
        }else{
            return eledesc.getPortinfos().getOutputport().getPortcount();
        }
    }


}
