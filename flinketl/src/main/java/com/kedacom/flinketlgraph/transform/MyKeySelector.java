package com.kedacom.flinketlgraph.transform;

import org.apache.flink.api.java.functions.KeySelector;

import java.util.Map;

public class MyKeySelector implements KeySelector<Object, Object> {
    private String keyname;

    public MyKeySelector(String keyname) throws Exception {
        this.keyname = keyname;
    }

    @Override
    public Object getKey(Object element) throws Exception {
        if (!(element instanceof Map)){
            throw new Exception("MyKeySelector input is not map object");
        }
        Map<String, Object> aaa = (Map<String, Object>)element;
        return aaa.get(this.keyname);
    }
}
