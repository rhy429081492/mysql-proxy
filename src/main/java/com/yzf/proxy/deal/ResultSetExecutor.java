package com.yzf.proxy.deal;

import com.yzf.proxy.change.Exchange;
import com.yzf.proxy.core.ConnectionSet;
import com.yzf.proxy.core.NioConnection;
import com.yzf.proxy.packet.ClientPacket;
import com.yzf.proxy.packet.ServerPacket;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class ResultSetExecutor implements Executor {
    @Override
    public ClientPacket execute(ServerPacket serverPacket) {
        ClientPacket packet = new ClientPacket();
        String methodName = serverPacket.getMethodName();
        NioConnection nio = new ConnectionSet().getNioConnection(serverPacket.getId());
        Object[] args = serverPacket.getArgs();
        Class<?>[] types = Exchange.exchange(args);
        ResultSet rs = nio.getResultSet(serverPacket.getSid());
        Class<?> clazz = rs.getClass();
        try {
            Method m = clazz.getMethod(methodName, types);
            Object o = m.invoke(rs, args);
            if(methodName.equals("getMetaData")) {
                nio.addResultSetMetaData(serverPacket.getSid(), (ResultSetMetaData) o);
                packet.setId(nio.getId());
                packet.setSuccessful(true);
                packet.setReturnObject(null);
                packet.setErrorInfo("");
            }else if(methodName.equals("getStatement")) {
                int id = nio.addStatement((Statement) o);
                packet.setId(nio.getId());
                packet.setSuccessful(true);
                packet.setReturnObject(id);
                packet.setErrorInfo("");
            }else {
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
