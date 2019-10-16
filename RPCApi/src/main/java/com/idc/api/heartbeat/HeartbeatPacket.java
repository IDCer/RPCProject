package com.idc.api.heartbeat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HeartbeatPacket implements Serializable {
    private long time;
    private Map<String,Object> info=new HashMap<String,Object>();
    private String srcAdress;//rpcServer ip加端口

    public HeartbeatPacket(long time, Map<String, Object> info, String srcAdress) {
        this.time = time;
        this.info = info;
        this.srcAdress = srcAdress;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }
}
