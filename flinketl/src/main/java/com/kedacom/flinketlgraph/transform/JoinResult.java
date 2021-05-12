package com.kedacom.flinketlgraph.transform;

import java.util.Map;

public class JoinResult {
    Map<String, Object> objleft;
    Map<String, Object> objright;

    public JoinResult(Map<String, Object> objleft, Map<String, Object> objright) {
        this.objleft = objleft;
        this.objright = objright;
    }

    public Map<String, Object> getObjleft() {
        return objleft;
    }

    public void setObjleft(Map<String, Object> objleft) {
        this.objleft = objleft;
    }

    public Map<String, Object> getObjright() {
        return objright;
    }

    public void setObjright(Map<String, Object> objright) {
        this.objright = objright;
    }

    @Override
    public String toString() {
        return "JoinResult{" +
                "objleft=" + objleft +
                ", objright=" + objright +
                '}';
    }
}
