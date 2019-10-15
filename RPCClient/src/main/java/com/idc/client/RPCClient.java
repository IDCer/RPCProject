package com.idc.client;

import com.idc.client.proxy.RPCClientProxy;

import java.lang.reflect.Proxy;

public class RPCClient {
    public <T> T getProxy(final Class<T> interfaceClass, final String host, final int port)  {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new RPCClientProxy(host, port));
    }
}
