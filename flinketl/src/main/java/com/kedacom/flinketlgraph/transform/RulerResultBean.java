package com.kedacom.flinketlgraph.transform;

import java.util.Map;

public class RulerResultBean {
    Map<String, Object> rulermap;
    Map<String, Object> datamap;

    public RulerResultBean(Map<String, Object> rulermap, Map<String, Object> datamap) {
        this.rulermap = rulermap;
        this.datamap = datamap;
    }

    public Map<String, Object> getRulermap() {
        return rulermap;
    }

    public void setRulermap(Map<String, Object> rulermap) {
        this.rulermap = rulermap;
    }

    public Map<String, Object> getDatamap() {
        return datamap;
    }

    public void setDatamap(Map<String, Object> datamap) {
        this.datamap = datamap;
    }

    @Override
    public String toString() {
        return "RulerResultBean{" +
                "rulermap=" + rulermap +
                ", datamap=" + datamap +
                '}';
    }
}
