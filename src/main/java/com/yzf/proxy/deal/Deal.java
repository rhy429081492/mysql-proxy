package com.yzf.proxy.deal;


import com.yzf.proxy.packet.ClientPacket;
import com.yzf.proxy.packet.ServerPacket;

public class Deal {
        public ClientPacket dealWith(ServerPacket sp){
        ClientPacket clientPacket = new ClientPacket();
        Executor executor = null;
        String className = sp.getClassName();
        if ("Driver".equals(className)){
            executor = new DriverExecutor();
        } else if ("Connection".equals(className)){
            executor = new ConnectionExecutor();
        } else if ("DatabaseMetaData".equals(className)){
            executor = new DatabaseMetaDataExecutor();
        } else if ("Statement".equals(className)){
            executor = new StatementExecutor();
        } else if ("ResultSet".equals(className)){
            executor = new ResultSetExecutor();
        } else if ("PreparedStatement".equals(className)){
            executor = new PreparedStatementExecutor();
        } else if ("ResultSetMetaData".equals(className)){
            executor = new ResultSetMetaDataExecutor();
        } else if ("ParameterMetaData".equals(className)){
            executor = new ParameterMetaDataExecutor();
        }
        clientPacket = executor.execute(sp);//处理数据包
        return clientPacket;
    }
}
