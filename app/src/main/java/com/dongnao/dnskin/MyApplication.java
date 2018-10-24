package com.dongnao.dnskin;

import android.app.Application;

import com.longfor.skin.core3.SkinManager;


/**
 * @author Lance
 * @date 2018/3/8
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
