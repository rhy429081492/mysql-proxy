package com.yzf.proxy.deal;


import com.yzf.proxy.change.Exchange;
import com.yzf.proxy.core.ConnectionSet;
import com.yzf.proxy.core.NioConnection;
import com.yzf.proxy.packet.ClientPacket;
import com.yzf.proxy.packet.ServerPacket;

import java.lang.reflect.Method;
import java.sql.ParameterMetaData;

public class ParameterMetaDataExecutor implements Executor {
    @Override
    public ClientPacket execute(ServerPacket serverPacket) {
        ClientPacket packet = new ClientPacket();
        String methodName = serverPacket.getMethodName();
        NioConnection nio = new ConnectionSet().getNioConnection(serverPacket.getId());
        Object[] args = serverPacket.getArgs();
        Class<?>[] types = Exchange.exchange(args);
        ParameterMetaData pmd = nio.getParameterMetaData(serverPacket.getSid());
        Class<?> clazz = pmd.getClass();
        try {
            Method m = clazz.getMethod(methodName, types);
            Object o = m.invoke(pmd, args);
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
