package com.nnyy.seafood;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/1/6 0006.
 */

public class JsonMananger {
    static {
        TypeUtils.compatibleWithJavaBean = true;
    }

    private static final String tag = JsonMananger.class.getSimpleName();

    /**
     * 将json字符串转换成java对象
     *
     * @param json
     * @param cls
     * @return
     * @throws Exception
     */
    public static <T> T jsonToBean(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    /**
     * 将json字符串转换成java List对象
     *
     * @param json
     * @param cls
     * @return
     * @throws Exception
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) throws Exception {
        return JSON.parseArray(json, cls);
    }

    /**
     * 将bean对象转化成json字符串
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String beanToJson(Object obj) throws Exception {
        String result = JSON.toJSONString(obj);
        Log.e(tag, "beanToJson: " + result);
        return result;
    }

//    public static String getMsgBodyText(String obj) {
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(obj);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String ret = jsonObject.optString("text");
//        return ret;
//    }



}
