package com.yzf.proxy.service;


import com.yzf.proxy.core.ProxyCore;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class ProxyServer {
        public ProxyServer(){
            File file = new File("src/mysql-proxy.xml");
            try{
                ProxyCore.initialProxyCore(file);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
}
