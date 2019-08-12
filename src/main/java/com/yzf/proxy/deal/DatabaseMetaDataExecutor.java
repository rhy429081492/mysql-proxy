package com.yzf.proxy.deal;



import com.yzf.proxy.change.Exchange;
import com.yzf.proxy.core.ConnectionSet;
import com.yzf.proxy.core.NioConnection;
import com.yzf.proxy.packet.ClientPacket;
import com.yzf.proxy.packet.ServerPacket;

import java.lang.reflect.Method;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class DatabaseMetaDataExecutor implements Executor{
    @Override
    public ClientPacket execute(ServerPacket serverPacket) {
        ClientPacket packet = new ClientPacket();
        String methodName = serverPacket.getMethodName();
        NioConnection nio = new ConnectionSet().getNioConnection(serverPacket.getId());
        Object[] args = serverPacket.getArgs();
        Class<?>[] types = Exchange.exchange(args);
        DatabaseMetaData dbmd = nio.getDbmd();
        Class<?> clazz = dbmd.getClass();
        try {
            Method m = clazz.getMethod(methodName, types);
            Object o = m.invoke(dbmd, args);
            if("getProcedures".equals(methodName)||//返回不可序列化对象的处理方法
                    "getProcedures".equals(methodName)||
                    "getProcedureColumns".equals(methodName)||
                    "getTables".equals(methodName)||
                    "getSchemas".equals(methodName)||
                    "getCatalogs".equals(methodName)||
                    "getTableTypes".equals(methodName)||
                    "getColumns".equals(methodName)||
                    "getColumnPrivileges".equals(methodName)||
                    "getTablePrivileges".equals(methodName)||
                    "getBestRowIdentifier".equals(methodName)||
                    "getVersionColumns".equals(methodName)||
                    "getPrimaryKeys".equals(methodName)||
                    "getImportedKeys".equals(methodName)||
                    "getExportedKeys".equals(methodName)||
                    "getCrossReference".equals(methodName)||
                    "getTypeInfo".equals(methodName)||
                    "getIndexInfo".equals(methodName)||
                    "getUDTs".equals(methodName)||
                    "getSuperTypes".equals(methodName)||
                    "getAttributes".equals(methodName)||
                    "getSchemas".equals(methodName)||
                    "getClientInfoProperties".equals(methodName)||
                    "getFunctions".equals(methodName)||
                    "getFunctionColumns".equals(methodName)||
                    "getPseudoColumns".equals(methodName)) {
                int id = nio.addResultSet((ResultSet) o);
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
