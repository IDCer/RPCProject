package com.idc.api.interfaces.impl;

import com.idc.api.interfaces.RPCRegistryCenter;
import com.idc.api.interfaces.RPCRegistryCenterConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class RPCRegisterCenterImpl implements RPCRegistryCenter {

    private CuratorFramework curatorFramework;

    {
        curatorFramework = CuratorFrameworkFactory.builder().connectString(RPCRegistryCenterConfig.CONNECTING_STR)
        .sessionTimeoutMs(RPCRegistryCenterConfig.SESSION_TIMEOUT).retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        curatorFramework.start();
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        String serviceNodePath = RPCRegistryCenterConfig.NAMESPACE + "/" + serviceName;

        try {
            if (curatorFramework.checkExists().forPath(serviceNodePath) == null) {
                // 持久化节点
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(serviceNodePath, RPCRegistryCenterConfig.DEFAULT_VALUE);

                // 注册的服务的节点路径
                String addressPath = serviceNodePath + "/" + serviceAddress;

                // 临时节点
                String rsNode = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(addressPath, RPCRegistryCenterConfig.DEFAULT_VALUE);

                System.out.println("服务注册成功!");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
