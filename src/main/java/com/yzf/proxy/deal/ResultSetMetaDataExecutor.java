package com.yzf.proxy.deal;

import com.yzf.proxy.change.Exchange;
import com.yzf.proxy.core.ConnectionSet;
import com.yzf.proxy.core.NioConnection;
import com.yzf.proxy.packet.ClientPacket;
import com.yzf.proxy.packet.ServerPacket;

import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;

public class ResultSetMetaDataExecutor implements Executor {
    @Override
    public ClientPacket execute(ServerPacket serverPacket) {
        ClientPacket packet = new ClientPacket();
        String methodName = serverPacket.getMethodName();
        NioConnection nio = ConnectionSet.getNioConnection(serverPacket.getId());
        Object[] args = serverPacket.getArgs();
        Class<?>[] types = Exchange.exchange(args);
        ResultSetMetaData rsmd = nio.getResultSetMetaData(serverPacket.getSid());
        Class<?> clazz = rsmd.getClass();
        try {
            Method m = clazz.getDeclaredMethod(methodName, types);
            Object o = m.invoke(rsmd, args);
            packet.setId(nio.getId());
            packet.setSuccessful(true);
            packet.setReturnObject(o);
            packet.setErrorInfo("");
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
