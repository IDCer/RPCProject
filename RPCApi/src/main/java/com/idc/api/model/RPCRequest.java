package com.idc.api.model;

import com.idc.api.interfaces.RPCSignal;

import java.io.Serializable;

/**
 * 客户端和服务端传输简讯的介质
 */

public class RPCRequest implements RPCSignal, Serializable {
    // 类名
    private String className;

    // 方法名
    private String methodName;

    // 参数列表
    private Object [] parameters;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
