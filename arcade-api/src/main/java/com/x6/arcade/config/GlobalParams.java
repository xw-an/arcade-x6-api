package com.x6.arcade.config;

import java.util.HashMap;
import java.util.Map;

public class GlobalParams {

    private static ThreadLocal<Map<String,Object>> globalParams = new ThreadLocal<>();

    public static void setParams(String key,Object value) {
        getParams().put(key,value);
    }

    public static Map<String,Object> getParams() {
        if (globalParams.get() == null) {
            globalParams.set(new HashMap<>());
        }
        return globalParams.get();
    }

    public static void remove() {
        globalParams.remove();
    }
}
