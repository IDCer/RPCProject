package com.idc.api.service;

import com.idc.api.interfaces.RPCAnnotation;

@RPCAnnotation(value=MessageService.class)
public interface MessageService {
    public String sendMessage(String message);
}
