package com.idc.zookeeper.thread;

import com.idc.api.heartbeat.HeartbeatPacket;
import com.idc.api.interfaces.RPCAnnotation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HeartbeatHandler implements Runnable {

    public static Map<String, Long> record=new HashMap<String, Long>();
    @Override
    public void run() {
        try {
            ServerSocket serverSocket=new ServerSocket(HeartbeatHandlerConfig.port);
            while(true){
                Socket socket=serverSocket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processHeartbeat(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois= new ObjectInputStream(socket.getInputStream());
        HeartbeatPacket packet=(HeartbeatPacket)ois.readObject();

        System.out.println("接收到"+packet.getSrcAdress()+"的信号，服务名如下：");
        for(String serviceName:packet.getInfo().keySet()){
            System.out.println(serviceName);
            record.put(serviceName,packet.getTime());
        }



    }
}
