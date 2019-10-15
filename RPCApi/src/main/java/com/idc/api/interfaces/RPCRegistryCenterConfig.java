package com.idc.api.interfaces;

public interface RPCRegistryCenterConfig {
    // zookeeper address
    public String CONNECTING_STR = "192.169.220.136,192.168.220.137";

    public int SESSION_TIMEOUT = 4000;

    // register center namespace
    public String NAMESPACE = "/rpcNode";

    // node feature
    byte[] DEFAULT_VALUE = "0".getBytes();
}
