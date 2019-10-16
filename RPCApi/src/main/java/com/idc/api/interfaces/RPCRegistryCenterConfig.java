package com.idc.api.interfaces;

public interface RPCRegistryCenterConfig {
    // zookeeper地址
//    public String CONNECTING_STR = "192.169.220.136,192.168.220.137";
    public String CONNECTING_STR = "127.0.0.1:2181";

    // 每个会话session连接超时阈值
    public int SESSION_TIMEOUT = 4000;

    // 注册中心中的命名空间
    public String NAMESPACE = "/rpcNode";

    // 节点默认值
    public byte[] DEFAULT_VALUE = "0".getBytes();
}
