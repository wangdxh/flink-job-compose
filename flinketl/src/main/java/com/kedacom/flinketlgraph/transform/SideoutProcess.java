package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.GroovyInstanceUtils;
import com.kedacom.flinketlgraph.json.Graph;
import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Sideouttag;
import com.kedacom.flinketlgraph.json.Sideouttransformspec;
import groovy.lang.GroovyObject;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.FileUtils;
import org.apache.flink.util.OutputTag;

import java.io.File;
import java.util.*;


public class SideoutProcess extends ProcessFunction<Object, Object>
{
    private static final long serialVersionUID = 3921539009821198563L;
    private Sideouttransformspec spec;
    private transient Map<String, List<Object>> maptagstodatalist;
    private transient Map<String, OutputTag<Object>> maptagstooutputtag;
    private transient GroovyObject instance;
    private String strgroovy = "";
    private String uid = "";
    private String jobname = "";

    //private static final String sideoutDefault = "default";

    public SideoutProcess(Graphnode trans, Graph graph) throws Exception {
        this.uid = trans.getNodeid();
        if (uid.length()==0){
            throw new Exception("sideout uid length is 0");
        }
        this.jobname = graph.getGraphconfig().getJobname();

        ObjectMapper mapper = new ObjectMapper();
        spec = mapper.convertValue(trans.getElementconfig(), Sideouttransformspec.class);
        //String strgroovy = "";
        String strpath = spec.getGroovyfilepath();
        if (strpath != null && strpath.length() > 0){
            strgroovy = FileUtils.readFileUtf8(new File(strpath));
        }

        String strcontent = spec.getGroovyfilecontent();
        if (strcontent != null && strcontent.length() > 0){
            Base64.Decoder decoder = Base64.getDecoder();
            strgroovy = new String(decoder.decode(strcontent), "UTF-8");
        }
        if (strgroovy.length() == 0){
            throw new Exception("groovyflatmap groovy file no content");
        }
    }

    @Override
    public void open(Configuration parameters) throws Exception
    {
        super.open(parameters);

        maptagstodatalist = new HashMap<>();
        maptagstooutputtag = new HashMap<>();
        List<Sideouttag> taglist = spec.getSideouttags();
        taglist.forEach(item->{
            maptagstooutputtag.put(item.getTagname(), new OutputTag<Object>(item.getTagname()){});
            maptagstodatalist.put(item.getTagname(), new LinkedList<>());
        });

        //maptagstodatalist.put(sideoutDefault, new LinkedList<>());
        /*GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        Class groovyclass = classLoader.parseClass(strgroovy);
        instance = (GroovyObject)groovyclass.newInstance();
        classLoader.close();*/

        Class groovyclass  = GroovyInstanceUtils.getGroovyClass(strgroovy, uid, jobname);
        instance = (GroovyObject)groovyclass.newInstance();
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void processElement(Object value, Context ctx, Collector<Object> out) throws Exception {
        try {
            GroovyInstanceUtils.invodeMethod(instance, "sideout", value, maptagstodatalist);
        }catch (Exception e){
            if (this.spec.getThrowgroovyexception()){
                throw  e;
            }else{
                e.printStackTrace();
            }
        }
        for (Map.Entry<String, List<Object>> entry : maptagstodatalist.entrySet()) {
            List<Object> listobj = entry.getValue();
            // 用户使用起来更加简洁，去掉default
            
            /*if (entry.getKey().equals(sideoutDefault)){
                for (Object o : listobj) {
                    out.collect(o);
                }
            }else{
                OutputTag<Object> tag = maptagstooutputtag.get(entry.getKey());
                for (Object o : listobj) {
                    ctx.output(tag, o);
                }
            }*/
            OutputTag<Object> tag = maptagstooutputtag.get(entry.getKey());
            for (Object o : listobj) {
                ctx.output(tag, o);
            }
            listobj.clear();
        }
    }

}

