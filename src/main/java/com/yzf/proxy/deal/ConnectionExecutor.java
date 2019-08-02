package com.yzf.proxy.deal;



import com.yzf.proxy.change.Exchange;
import com.yzf.proxy.core.ConnectionSet;
import com.yzf.proxy.core.NioConnection;
import com.yzf.proxy.packet.ClientPacket;
import com.yzf.proxy.packet.ServerPacket;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ConnectionExecutor implements Executor{
    @Override
    public ClientPacket execute(ServerPacket serverPacket) {
        ClientPacket packet = new ClientPacket();
        String methodName = serverPacket.getMethodName();
        NioConnection nio = ConnectionSet.getNioConnection(serverPacket.getId());
        if(methodName.equals("isClosed")&&nio == null){
            packet.setId(serverPacket.getId());
            packet.setSuccessful(true);
            packet.setReturnObject(true);
            packet.setErrorInfo("");
            return packet;
        }
        Object[] args = serverPacket.getArgs();
        Class<?>[] types = Exchange.exchange(args);
        Connection conn = nio.getConn();
        Class<?> clazz = conn.getClass();
        try {
            Method m = clazz.getDeclaredMethod(methodName, types);
            Object o = m.invoke(conn, args);
            if(methodName.equals("createStatement")) {
                int sid = nio.addStatement((Statement)o);
                packet.setId(nio.getId());
                packet.setSuccessful(true);
                packet.setReturnObject(sid);
                packet.setErrorInfo("");
            } else if(methodName.equals("prepareStatement")) {
                int sid = nio.addStatement((PreparedStatement)o);
                packet.setId(nio.getId());
                packet.setSuccessful(true);
                packet.setReturnObject(sid);
                packet.setErrorInfo("");
            } else if(methodName.equals("prepareCall")) {
                int sid = nio.addStatement((CallableStatement)o);
                packet.setId(nio.getId());
                packet.setSuccessful(true);
                packet.setReturnObject(sid);
                packet.setErrorInfo("");
            } else if(methodName.equals("getMetaData")) {
                nio.setDbmd(nio.getConn().getMetaData());
                packet.setId(nio.getId());
                packet.setSuccessful(true);
                packet.setReturnObject(null);
                packet.setErrorInfo("");
            } else if(methodName.equals("close")){
                ConnectionSet.removeNioCOnnection(serverPacket.getId());
                packet.setId(nio.getId());
                packet.setSuccessful(true);
                packet.setReturnObject(null);
                packet.setErrorInfo("");
            }
            else {
                packet.setId(nio.getId());
                packet.setSuccessful(true);
                packet.setReturnObject(o);
                packet.setErrorInfo("");
            }
        }catch (Exception e) {
            e.printStackTrace();
            packet.setId(nio.getId());
            packet.setSuccessful(false);
            packet.setReturnObject(null);
            packet.setErrorInfo(e.getMessage());
        }
        return packet;
    }
}
