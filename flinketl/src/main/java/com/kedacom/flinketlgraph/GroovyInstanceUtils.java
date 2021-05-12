package com.kedacom.flinketlgraph;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GroovyInstanceUtils {
    private static final Logger logger = LoggerFactory.getLogger(GroovyInstanceUtils.class);

    public static Object invodeMethod(GroovyObject instance, String funname, Object... params){
        return instance.invokeMethod(funname, params);
    }
    private static Class create(String strgroovy) throws Exception{
        GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        Class groovyclass = classLoader.parseClass(strgroovy);
        //GroovyObject instance = (GroovyObject)groovyclass.newInstance();
        classLoader.close();
        return groovyclass;
    }

    // 解决standalone，job重启时groovy class 在metaspace中缓存的问题,只要groovy的hash一致，就认为是同一个文件
    public static Class getGroovyClass(String strgroovy, String uid, String jobname) throws Exception {
        boolean blocal = false;
        // when use full, every time job restart map will be a new hashcode.
        // may be when use region this could be again,
        if (blocal){
            return create(strgroovy);
        }else{
            String key = jobname + "_" + uid;
            String newhash = Integer.toString(Math.abs(strgroovy.hashCode()));

            synchronized (g_mapgroovyobject){
                if (g_mapgroovyobject.containsKey(key)){
                    GroovyObjectAndBean bean = g_mapgroovyobject.get(key);
                    if (newhash.equals(bean.getHashcode())){
                        logger.info("use cached groovyclass {} from {} {} mapsize {} hash {}",
                                bean.getObj().getName(), jobname, uid, g_mapgroovyobject.size(), Integer.toHexString(g_mapgroovyobject.hashCode()));
                        return bean.getObj();
                    }
                }
                Class cls = create(strgroovy);
                g_mapgroovyobject.put(key, new GroovyObjectAndBean(cls, newhash));
                logger.info("create groovyclass {} from {} {} mapsize {} hash {}",
                        cls.getName(), jobname, uid, g_mapgroovyobject.size(), Integer.toHexString(g_mapgroovyobject.hashCode()));
                return cls;
            }
        }
    }
    private static final Map<String, GroovyObjectAndBean> g_mapgroovyobject = new HashMap<>();

    static class GroovyObjectAndBean {
        private Class obj;
        private String hashcode;

        public GroovyObjectAndBean() {
        }

        public GroovyObjectAndBean(Class obj, String hashcode) {
            this.obj = obj;
            this.hashcode = hashcode;
        }

        public Class getObj() {
            return obj;
        }

        public void setObj(Class obj) {
            this.obj = obj;
        }

        public String getHashcode() {
            return hashcode;
        }

        public void setHashcode(String hashcode) {
            this.hashcode = hashcode;
        }
    }
}
