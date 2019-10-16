package com.idc.api.interfaces.impl;

import com.idc.api.interfaces.RPCRegistryCenter;
import com.idc.api.interfaces.RPCRegistryCenterConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class RPCRegisterCenterImpl implements RPCRegistryCenter {

    private CuratorFramework curatorFramework;

    {
        // 运行服务器期间只会执行一次
        curatorFramework = CuratorFrameworkFactory.builder().connectString(RPCRegistryCenterConfig.CONNECTING_STR)
        .sessionTimeoutMs(RPCRegistryCenterConfig.SESSION_TIMEOUT).retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();

        // 启动线程
        curatorFramework.start();
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        System.out.println("开始注册服务...");

        // 服务节点路径
        String serviceNodePath = RPCRegistryCenterConfig.NAMESPACE + "/" + serviceName;
        System.out.println("服务节点路径:" + serviceNodePath);

        try {

            // 判断节点是否存在
            Stat stat = curatorFramework.checkExists().forPath(serviceNodePath);

            if (stat == null) {
                System.out.println("服务节点{" + serviceNodePath + "}不存在,开始创建");

                // 持久化节点
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(serviceNodePath, RPCRegistryCenterConfig.DEFAULT_VALUE);

                // 注册的服务的节点路径
                String addressPath = serviceNodePath + "/" + serviceAddress;

                // 临时节点
                String rsNode = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(addressPath, RPCRegistryCenterConfig.DEFAULT_VALUE);
                System.out.println("服务注册成功:" + rsNode);
            } else {
                System.out.println("服务节点{" + serviceNodePath + "}已存在!");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
