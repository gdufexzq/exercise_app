package com.gdufe.exercise_app.util;

import org.apache.http.HttpException;

public class OpenIdUtil {


    public static String getJsonObject(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx0df6f01559cde85d" +
                "&secret=d1668fdf11f98c652b6813a6a5b177b8" +
                "&js_code=" + code +
                "&grant_type=authorization_code";
        String jsonObject = null;
        try {
            jsonObject = HttpUtils.get(url, null);
            System.out.println(jsonObject);
            return jsonObject;
        } catch (HttpException e) {
            e.printStackTrace();
        }
        return null;
    }

}
