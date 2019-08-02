package com.yzf.proxy.change;
/*
反射调用优化
 */
public class Exchange {
    public static Class<?>[] exchange(Object[] args) {
        if(args == null) {
            return null;
        }
        int length = args.length;
        Class<?>[] types = new Class<?>[length];
        for(int i=0;i<length;i++) {
            if(args[i] == null){
                types[i] = String.class;
                i++;
            }
            String name = args[i].getClass().getName();
            if("java.lang.Boolean".equals(name)) {
                types[i] = boolean.class;
            } else if("java.lang.Integer".equals(name)) {
                types[i] = int.class;
            } else if("java.lang.Float".equals(name)) {
                types[i] = float.class;
            } else if("java.lang.Double".equals(name)) {
                types[i] = double.class;
            } else if("java.lang.Long".equals(name)) {
                types[i] = long.class;
            } else if("java.lang.Short".equals(name)) {
                types[i] = short.class;
            } else if("java.lang.Byte".equals(name)) {
                types[i] = byte.class;
            }
            else {
                types[i] = args[i].getClass();
            }
        }
        return types;
    }
}
