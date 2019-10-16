package com.idc.client;

import com.idc.api.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RpcClientApplicationTests {

	@Test
	public void contextLoads() {
		// 从客户端处得到一个关于该接口的代理对象
		MessageService messageService = new RPCClient().getProxy(MessageService.class, "127.0.0.1", 11234);

		// 输入服务参数,并得到结果
		String result = messageService.sendMessage("hello guys");

		// 输出结果
		System.out.println(result);
	}

}
