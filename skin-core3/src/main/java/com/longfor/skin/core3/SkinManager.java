package com.longfor.skin.core3;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.TextView;

import com.longfor.skin.core3.util.SkinPreference;
import com.longfor.skin.core3.util.SkinResource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;

/**
 * 用来进行对皮肤加载的管理
 *
 * @author zhanghaitao
 * created at 2018/10/22  15:21
 */
public class SkinManager  extends Observable{
    private static SkinManager instance;
    private Application application;

    public static SkinManager getInstance() {
        return instance;
    }

    private SkinManager(Application application) {
        this.application = application;
        SkinPreference.init(application);
        SkinResource.init(application);
        application.registerActivityLifecycleCallbacks(new SkinActivityLifecycle());

        loadSkin(SkinPreference.getInstance().getSkin());
    }

    public static void init(Application application) {
        synchronized (SkinManager.class) {
            if (null == instance) {
                instance = new SkinManager(application);
            }
        }
    }

    //加载皮肤包
    public void loadSkin(String path) {
        if(TextUtils.isEmpty(path)){
            SkinPreference.getInstance().setSkin("");
            SkinResource.getInstance().reset();
        }else{
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                // 添加资源进入资源管理器
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String
                        .class);
                addAssetPath.setAccessible(true);
                addAssetPath.invoke(assetManager, path);

                Resources resources = application.getResources();
                // 横竖、语言
                Resources skinResource = new Resources(assetManager, resources.getDisplayMetrics(),
                        resources.getConfiguration());
                //获取外部Apk(皮肤包) 包名
                PackageManager mPm = application.getPackageManager();
                PackageInfo info = mPm.getPackageArchiveInfo(path, PackageManager
                        .GET_ACTIVITIES);
                String packageName = info.packageName;
                SkinResource.getInstance().applySkin(skinResource, packageName);
                //保存当前使用的皮肤包
                SkinPreference.getInstance().setSkin(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setChanged();
        notifyObservers();
    }

}
