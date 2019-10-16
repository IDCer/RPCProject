package com.idc.api.heartbeat;

import com.idc.server.RPCServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HeartbeatClient implements Runnable {
    private RPCServer rpcServer;

    public HeartbeatClient(RPCServer rpcServer) {
        this.rpcServer = rpcServer;
    }

    @Override
    public void run() {
        long currentTime=System.currentTimeMillis();
        HeartbeatPacket packet =new HeartbeatPacket(currentTime,rpcServer.getHandlerMapping(),rpcServer.getAddressService());
        /**
         * 发送心跳包
         */
        System.out.println("发送心跳包中");
        sendMessage(packet);
        System.out.println("发送心跳包成功");
    }

    public void sendMessage(Object obj) {
        String [] ipAndPort=HeartbeatConfig.CONNECTING_STR.split(":");
        try {
            Socket socket =new Socket(ipAndPort[0],Integer.parseInt(ipAndPort[1]));
            OutputStream outputStream=socket.getOutputStream();
            ObjectOutputStream os= new ObjectOutputStream(outputStream);
            os.writeObject(obj);
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
