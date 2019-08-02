package com.yzf.proxy.packet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ClientPacket {
    private int id;//线程号
    private boolean isSuccessful;//是否成功
    private Object returnObject;//返回对象
    private String errorInfo;//错误信息

    public ClientPacket() {
    }

    public String getString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("isSuccessful", isSuccessful);
        jsonObject.put("returnObject", returnObject);
        jsonObject.put("errorInfo",errorInfo);
        return jsonObject.toJSONString();
    }

    public ClientPacket(String cp){
        JSONObject jsonObject = JSON.parseObject(cp);
        id = jsonObject.getIntValue("id");
        isSuccessful = jsonObject.getBoolean("isSuccessful");
        returnObject = jsonObject.get("returnObject");
        errorInfo = jsonObject.getString("errorInfo");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
