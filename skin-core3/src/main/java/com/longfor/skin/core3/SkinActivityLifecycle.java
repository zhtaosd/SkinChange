package com.longfor.skin.core3;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;

import com.longfor.skin.core3.util.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
* 用来对activity的生命周期进行监听
* @author zhanghaitao
* created at 2018/10/22  15:56
*/
public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks{
    HashMap<Activity, SkinLayoutFactory> mLayoutFactoryMap = new HashMap<>();
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //更新状态栏
        SkinThemeUtils.updateStatusBar(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        //设置变量防止设置工厂时候出现异常
        try {
            Field field = LayoutInflater.class.getField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(inflater,false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //进行工厂的设置
        Typeface typeface = SkinThemeUtils.getSkinTypeface(activity);
        SkinLayoutFactory factory = new SkinLayoutFactory(activity,typeface);
        LayoutInflaterCompat.setFactory2(inflater,factory);

        SkinManager.getInstance().addObserver(factory);
        mLayoutFactoryMap.put(activity,factory);

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SkinLayoutFactory skinLayoutFactory = mLayoutFactoryMap.remove(activity);
        SkinManager.getInstance().deleteObserver(skinLayoutFactory);
    }
}
