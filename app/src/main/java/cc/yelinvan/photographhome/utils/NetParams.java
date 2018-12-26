package cc.yelinvan.photographhome.utils;

import android.content.Context;

import org.xutils.http.RequestParams;

import cc.yelinvan.photographhome.Constant;

/**
 * Create by Johnson on 2018年12月26日10:55:46
 */
public class NetParams extends RequestParams {
    public NetParams(Context context, String url, int timeOut){
        super(url);

        setConnectTimeout(timeOut==0?30*1000:timeOut);
        addHeader("Type","android");
        long timestamp = System.currentTimeMillis()/1000;
        addHeader("Timestamp", timestamp+"");
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(context, Constant.LOGININFO);
        String token = (String) sharedPreferencesHelper.getSharedPreference(Constant.TOKEN, "");
        token = "Bearer "+token;
        addHeader("Accept","application/json");
        addHeader("Authorization",token);
    }

}
