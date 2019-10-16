package com.idc.api.service;

import com.idc.api.interfaces.RPCAnnotation;

/**
 * 发送信息服务接口
 */

@RPCAnnotation(value=MessageService.class)
public interface MessageService {
    public String sendMessage(String message);
}
