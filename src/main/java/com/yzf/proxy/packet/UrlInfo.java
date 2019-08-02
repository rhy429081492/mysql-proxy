package com.yzf.proxy.packet;

public class UrlInfo {
    public String front = "";
    public String host = "";
    public String port = "";
    public String database = "";
    public String other = "";

    public UrlInfo(String url) throws Exception{
        String str1 = url.substring(0, 13);
        front = str1;
        int i = 13;
        while(url.charAt(i)!=':' && i<url.length()) {
            i++;
        }
        if(i>13) {
            host = url.substring(13,i);
            i++;
        } else {
            throw new Exception("url格式错误");
        }
        int j=i;
        while(url.charAt(i)!='/' && i<url.length()) {
            i++;
        }
        if(i>j) {
            port = url.substring(j, i);
            i++;
        } else {
            throw new Exception("url格式错误");
        }
        j=i;
        while(i<url.length() && url.charAt(i)!='?') {
            i++;
        }
        if(i>j) {
            database = url.substring(j,i);
            i++;
        } else {
            throw new Exception("url格式错误");
        }
        if(i < url.length()) {
            other = url.substring(i,url.length());
        }
    }
}
