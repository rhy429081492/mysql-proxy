package com.yzf.proxy.deal;


import com.yzf.proxy.packet.ClientPacket;
import com.yzf.proxy.packet.ServerPacket;

public interface Executor {
    public ClientPacket execute(ServerPacket serverPacket);
}
