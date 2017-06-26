package org.templateproject.boot.controller;

import org.templateproject.boot.controller.support.MethodBootController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 基础控制类
 * Created by wuwenbin on 2017/6/4.
 */
public abstract class SimpleBootController extends MethodBootController {

    /**
     * 在没有勾选框checkbox多选的情况下，可以将Map<String,String[]>转为Map<String,String>
     *
     * @param requestWebMap 请求的web map
     * @return 普通java map
     */
    public Map<String, Object> arrayMapToStringMap(Map<String, String[]> requestWebMap) {
        Iterator<?> iterator = requestWebMap.entrySet().iterator();
        HashMap map;
        String key;
        String val;
        for (map = new HashMap(); iterator.hasNext(); map.put(key, val)) {
            Map.Entry entry = (Map.Entry) iterator.next();
            key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof String[]) {
                String[] strings = ((String[]) value);
                val = strings[0];
            } else {
                val = value.toString();
            }
        }
        return map;
    }


}
