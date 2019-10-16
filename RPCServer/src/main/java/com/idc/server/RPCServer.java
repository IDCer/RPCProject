package com.idc.server;

import com.idc.api.interfaces.RPCAnnotation;
import com.idc.api.interfaces.RPCRegistryCenter;
import com.idc.server.thread.ServerProcessHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RPCServer {

    // 注册中心
    private RPCRegistryCenter registryCenter;

    // 服务的发布地址
    private String addressService;

    // 服务名称和服务对象之间的关系,映射表
    private static final Map<String, Object> HANDLER_MAPPING = new HashMap<>();

    // 新的发布服务方法
    public void export() {
        // 获取端口
        int port = Integer.parseInt(addressService.split(":")[1]);

        System.out.println("注册中心开始注册并发布服务...");
        // 注册中心注册服务
        try (ServerSocket serverSocket = new ServerSocket(port)){
//            for(String interfaceName : HANDLER_MAPPING.keySet()) {
//                registryCenter.register(interfaceName, addressService);
//                System.out.println("服务注册成功!服务名:{" + interfaceName + "},地址:{" + addressService + "}");
//            }
            // 循环注册服务
            HANDLER_MAPPING.keySet().forEach(interfaceName->{
                registryCenter.register(interfaceName, addressService);
                System.out.println("服务注册成功!服务名:{" + interfaceName + "},地址:{" + addressService + "}");
            });

            while (true) {
                // 阻塞监听client端线程
                System.out.println("等待客户端请求...");
                Socket socket = serverSocket.accept();
                // 接收到一个客户端的请求
                System.out.println("接收到一个客户端请求 -> " + socket.getLocalAddress() + ":" + socket.getLocalPort());
                // 收到客户端线程,开始运行
                new Thread(new ServerProcessHandler(socket, HANDLER_MAPPING)).start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 绑定服务名称和服务对象
    public void bind(Object... services) {
        System.out.println("开始绑定服务...");
        for (Object service : services) {
            // 获取发布的服务接口
            System.out.println("需绑定的服务为:" + service);
            RPCAnnotation rpcAnnotation = service.getClass().getAnnotation(RPCAnnotation.class);
            if (rpcAnnotation == null) {
                System.out.println("rpcAnnotation is null");
                continue;
            }
            // 服务的名字
            String serviceName = rpcAnnotation.value().getName();

            // 添加到映射表中
            HANDLER_MAPPING.put(serviceName, service);
            System.out.println("服务绑定成功: {" + serviceName + ":" + service + "}");
        }
    }

    // 构造函数
    public RPCServer(RPCRegistryCenter rpcRegistryCenter, String addressService) {
        this.registryCenter = rpcRegistryCenter;
        this.addressService = addressService;
    }
}
