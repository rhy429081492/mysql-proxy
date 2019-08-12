package com.yzf.proxy.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionSet {//服务器连接镜像集，保存了所有客户端的连接镜像
    private static ConcurrentHashMap<Integer, NioConnection> ncs = new ConcurrentHashMap<Integer, NioConnection>();
    private static AtomicInteger id = new AtomicInteger(0);
    public int addNioConnection(NioConnection nio){
        int i = id.incrementAndGet();
        ncs.putIfAbsent(i, nio);
        return i;
    }
    public NioConnection getNioConnection(int id){
        return ncs.get(id);
    }
    public void removeNioCOnnection(int id){
        ncs.remove(id);
    }
}
