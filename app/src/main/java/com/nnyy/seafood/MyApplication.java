package com.nnyy.seafood;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,null);
        PlatformConfig.setWeixin("wx56005f539ef506f2", "13dc7a2393d13e9b0a9fab6bf717ac09");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("1513962813", "bf309c6ae2de7dd0617c3200427ba8dc","http://sns.whalecloud.com");
    }
}
