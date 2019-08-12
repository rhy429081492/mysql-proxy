package com.yzf.proxy.deal;


import com.yzf.proxy.core.ConnectionSet;
import com.yzf.proxy.core.NioConnection;
import com.yzf.proxy.core.ProxyCore;
import com.yzf.proxy.packet.ClientPacket;
import com.yzf.proxy.packet.ServerPacket;

public class DriverExecutor implements Executor{
    @Override
    public ClientPacket execute(ServerPacket spacket){
        ClientPacket packet = new ClientPacket();
        if (spacket.getMethodName().equals("connect")){
            String[] str = new String[3];
            str[0] = (String) spacket.getArgs()[0];
            str[1] = (String) spacket.getArgs()[1];
            str[2] = (String) spacket.getArgs()[2];
            //获取连接参数
            NioConnection nio = new NioConnection();
            int id = new ConnectionSet().addNioConnection(nio);
            //获取服务器线程号
            nio.setId(id);
            //设置线程号
            ProxyCore.getNioConnection(str[0],str[1],str[2], nio);
            //连接请求加入队列
            while(true){
                if(nio.getConn() != null){
                    //等待队列访问，获取到连接
                    packet.setId(nio.getId());
                    //向客户端返回线程号
                    packet.setSuccessful(true);
                    packet.setReturnObject(null);
                    packet.setErrorInfo("");
                    break;
                }
            }
        }
        return packet;
    }
}
