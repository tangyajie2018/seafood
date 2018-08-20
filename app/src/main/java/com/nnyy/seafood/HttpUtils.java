package com.nnyy.seafood;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    private String TAG="HttpUtils";
    private static HttpUtils instance;
    private static OkHttpClient client;
    private HttpUtils() {
        client = new OkHttpClient();
    }
    public static HttpUtils getInstall() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }
    private HttpFinish Lister;
    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new HttpUtils();
        }
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    private String wechatLoginUrl="http://wx.bjdglt.com/nnyyapi/api/wechat/app/login";
    public void wechatLogin(Map<String, String> data){
        FormBody.Builder builder = new FormBody.Builder()
//                .add("openid", "123121131231")
//                .add("unionid", "332211")
//                .add("nickname", data.get("name"))
//                .add("sex", data.get("gender"))
//                .add("headimgurl", data.get("iconurl"))
//                .add("access_token", MD5(data.get("uid")+"DpJwOidNzRhPCrQQ"))
//                .add("sign", MD5(data.get("uid")+"DpJwOidNzRhPCrQQ"));
                .add("openid", data.get("uid"))
                .add("unionid", data.get("uid"))
                .add("nickname", data.get("name"))
                .add("sex", data.get("gender"))
                .add("headimgurl", data.get("iconurl"))
                .add("access_token", MD5(data.get("uid")+"DpJwOidNzRhPCrQQ"))
                .add("refresh_token", MD5(data.get("uid")+"DpJwOidNzRhPCrQQ"))
                .add("sign", MD5(data.get("uid")+"DpJwOidNzRhPCrQQ"));
        RequestBody body = builder.build();
        final Request request = new Request.Builder()
                .url(wechatLoginUrl)
//                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject result = JSONObject.parseObject(response.body().string());
                int code=result.getIntValue("code");
                String id=result.getJSONObject("data").getString("ESC_ID");
                if(Lister!=null){
                    Lister.onLoginfinish(code,id);
                }
//                Log.d(TAG,"data" + response.body().string());
            }
        });
    }

    private String wechatPayUrl="http://wx.bjdglt.com/api/wxpay/pay?access_token=";
    public void getWechatOrder(String token,String out_trade_no,String body,String total_fee){
        FormBody.Builder builder = new FormBody.Builder()
                .add("out_trade_no", out_trade_no)
                .add("body", body)
                .add("total_fee", total_fee);
        RequestBody body1 = builder.build();
        Request request = new Request.Builder()
                .url(wechatLoginUrl)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .post(body1)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject result = JSONObject.parseObject(response.body().string());
                Log.d(TAG,"data" + result.toJSONString());
            }
        });
    }

    public interface HttpFinish{
        void onLoginfinish(int resultCode,String ESC_ID);
    }
    public void setLister(HttpFinish lister){
        this.Lister=lister;
    }


}
