package com.yzf.proxy.service;

import com.yzf.proxy.deal.Deal;
import com.yzf.proxy.packet.ClientPacket;
import com.yzf.proxy.packet.ServerPacket;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class Service {
    @RequestMapping(value = "/mysql-route/service", method = RequestMethod.POST)
    @ResponseBody
    public String deal(@RequestBody String data){//获取驱动上传数据包
        System.out.println(data);
        ServerPacket serverPacket = new ServerPacket(data);
        ClientPacket clientPacket = new ClientPacket();
        if (serverPacket != null){
            clientPacket = Deal.dealWith(serverPacket);
        } else {
            clientPacket.setId(serverPacket.getId());
            clientPacket.setSuccessful(false);
            clientPacket.setReturnObject(null);
            clientPacket.setErrorInfo("连接信息有误");
        }
        System.out.println(clientPacket.getString());
        return clientPacket.getString();
    }
}
