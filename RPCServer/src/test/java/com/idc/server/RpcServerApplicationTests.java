package com.idc.server;

import com.idc.api.interfaces.RPCRegistryCenter;
import com.idc.api.interfaces.impl.RPCRegisterCenterImpl;
import com.idc.server.service.MessageServiceImp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RpcServerApplicationTests {

	@Test
	public void contextLoads() {
		// 创建一个注册中心
		RPCRegistryCenter rpcRegistryCenter = new RPCRegisterCenterImpl();

		// 服务器运行ip地址
		String address = "127.0.0.1:11234";
		RPCServer rpcServer = new RPCServer(rpcRegistryCenter, address);

		// 绑定服务
		rpcServer.bind(new MessageServiceImp());

		// 发布服务
        rpcServer.export();
	}
}
