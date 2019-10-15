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

    // 服务名称和服务对象之间的关系
    private static final Map<String, Object> HANDLER_MAPPING = new HashMap<>();

    // 存储注册的服务列表
    private static List<Object> serviceList;

    // 发布服务
    public static void export(int port, Object services) throws Exception {
////        serviceList = Arrays.asList(services);
//        ServerSocket serverSocket = new ServerSocket(port);
//        Socket client = null;
//        while (true) {
//            // 阻塞等待输入
//            client = serverSocket.accept();
//            //每一个请求，启动一个线程处理
//            new Thread(new ServerProcessHandler(client, services)).start();
//        }
    }

    // 新的发布服务方法
    public void new_export() {
        // 启动一个服务监听
        // 获取端口
        int port = Integer.parseInt(addressService.split(":")[1]);
        System.out.println("port is :" + port);

        try (ServerSocket serverSocket = new ServerSocket(port)){
            HANDLER_MAPPING.keySet().forEach(interfaceName->{
                registryCenter.register(interfaceName, addressService);
                System.out.println("服务注册成功!服务名:{" + interfaceName + "},地址:{" + addressService + "}");
            });
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ServerProcessHandler(socket, HANDLER_MAPPING)).start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 绑定服务名称和服务对象
    public void bind(Object... services) {
        for (Object service : services) {
            // 获取发布的服务接口
            System.out.println("接收到的服务为:" + service);
            RPCAnnotation rpcAnnotation = service.getClass().getAnnotation(RPCAnnotation.class);
            if (rpcAnnotation == null) {
                System.out.println("rpcAnnotation is null");
                continue;
            }
            // publish service class
            String serviceName = rpcAnnotation.value().getName();

            // blind serviceName and service
            HANDLER_MAPPING.put(serviceName, service);
            System.out.println(HANDLER_MAPPING);
        }
    }

    // construct
    public RPCServer(RPCRegistryCenter rpcRegistryCenter, String addressService) {
        this.registryCenter = rpcRegistryCenter;
        this.addressService = addressService;
    }
}
