package com.idc.server.service;

import com.idc.api.interfaces.RPCAnnotation;
import com.idc.api.service.MessageService;

@RPCAnnotation(value=MessageService.class)
public class MessageServiceImp implements MessageService {
    @Override
    public String sendMessage(String message) {
        return "you say : " + message;
    }
}
