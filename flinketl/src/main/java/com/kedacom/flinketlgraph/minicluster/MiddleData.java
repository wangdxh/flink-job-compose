package com.kedacom.flinketlgraph.minicluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiddleData {
    private static Map<String, Map<String, List<Object>>> globalmapcollect = new HashMap<>();
    private static Map<String, Integer> globalmaxsize = new HashMap<>();

    public static void initJobData(String jobname, int nmaxsize){
        globalmapcollect.put(jobname, new HashMap<>());
        globalmaxsize.put(jobname, nmaxsize);
    }

    public static Map<String, List<Object>> getJobData(String jobname){
        Map<String, List<Object>> jobmap = globalmapcollect.get(jobname);
        return jobmap;
    }

    public static void clearJobData(String jobname){
        if (globalmapcollect.containsKey(jobname)){
            globalmapcollect.remove(jobname);
            globalmaxsize.remove(jobname);
        }
    }

    public static void addJobData(String jobname, String parentid, Object o){
        List<Object> listobject = globalmapcollect.get(jobname).get(parentid);
        int maxsize = globalmaxsize.get(jobname);
        if (listobject.size() < maxsize){
            listobject.add(o);
        }
    }
}
