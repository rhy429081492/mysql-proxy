package com.yzf.proxy.packet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ServerPacket {
    private int id;//线程id
    private int sid;//服务器对象id
    private String className;//申请类名
    private String methodName;//申请方法名
    private Object[] args;//申请方法参数

    public ServerPacket(int id, int sid, String className, String methodName, Object[] args) {
        this.id = id;
        this.sid = sid;
        this.className = className;
        this.methodName = methodName;
        this.args = args;
    }

    public  ServerPacket(String sp){
        JSONObject jsonObject = JSONObject.parseObject(sp);
        id = jsonObject.getIntValue("id");
        sid = jsonObject.getIntValue("sid");
        className = jsonObject.getString("className");
        methodName = jsonObject.getString("methodName");
        JSONArray jsonArray = jsonObject.getJSONArray("args");
        if (jsonArray != null){
            int len = jsonArray.size();
            args = new Object[len];
            for (int i=0;i<jsonArray.size();i++){
                args[i] = jsonArray.get(i);
            }
        }
    }

    public ServerPacket() {
    }

    public String getString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("sid", sid);
        jsonObject.put("className", className);
        jsonObject.put("methodName", methodName);
        jsonObject.put("args", args);
        return jsonObject.toJSONString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
