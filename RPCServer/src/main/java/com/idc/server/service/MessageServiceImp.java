package com.idc.server.service;

import com.idc.api.interfaces.RPCAnnotation;
import com.idc.api.service.MessageService;

/**
 * 发送信息接口的具体实现
 */

@RPCAnnotation(value=MessageService.class)
public class MessageServiceImp implements MessageService {
    @Override
    public String sendMessage(String message) {
        return "you say : " + message;
    }
}
