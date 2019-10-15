package com.idc.server.thread;

import com.idc.api.model.RPCRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;


public class ServerProcessHandler implements Runnable {
    // 持有一个客户端会话对象
    private Socket client;

    // 持有服务器发布服务
//    private Object service;

    // the service on server
    private Map<String, Object> handlerMap;

    // 构造函数
    public ServerProcessHandler(Socket client, Map<String, Object> handlerMap) {
        this.client = client;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        // 接受客户端信息流
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(client.getInputStream());

            // 反序列化
            RPCRequest rpcRequest = (RPCRequest) objectInputStream.readObject();
            Object result = invoke(rpcRequest);

            // 返回客户端
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject(result);

            // 刷新缓冲区
            objectOutputStream.flush();

            // 关闭流
            objectOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 检查输入流是否关闭
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object invoke(RPCRequest rpcRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("Server is invoking some method...");
        System.out.println("来自主机" + client.getLocalAddress() + ",端口:" +client.getLocalPort() + "调用了方法");
        // 获取客户端传来的参数
        Object [] parameters = rpcRequest.getParameters();
        Class [] parameterTypes = new Class[parameters.length];
        for (int i = 0, length = parameters.length; i < length; i++) {
            parameterTypes[i] = parameters[i].getClass();
            System.out.println("parameterTypes[i]" + parameterTypes[i]);
            System.out.println("parameters[i]" + parameters[i].getClass());
        }

        // get service from Map
        Object service = handlerMap.get(rpcRequest.getClassName());

        System.out.println("请求类名:" + rpcRequest.getClassName());
        System.out.println("Request is:" + rpcRequest);
        System.out.println("parameterTypes is:" + parameterTypes);

        System.out.println("服务类别:" + service.getClass());

        // 获取方法
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), parameterTypes);
        // 返回方法调用结果
        return method.invoke(service, parameters);

    }
}
