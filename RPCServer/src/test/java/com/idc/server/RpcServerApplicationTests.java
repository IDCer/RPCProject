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

		// address
		String address = "127.0.0.1:11234";
		RPCServer rpcServer = new RPCServer(rpcRegistryCenter, address);

		// blind service
		rpcServer.bind(new MessageServiceImp());

		// publish
//        rpcServer.new_export();
	}

}
