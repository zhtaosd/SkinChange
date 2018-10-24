package com.longfor.skin.core3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
* 工厂用来生成相关的view
* @author zhanghaitao
* created at 2018/10/22  16:04
*/
public class SkinLayoutFactory implements LayoutInflater.Factory2,Observer {
    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    //用来进行view构造方法的缓存，防止重复创建
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();

    private static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    SkinAttribute skinAttribute;
    public SkinLayoutFactory(){
        skinAttribute = new SkinAttribute();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //反射 classloader
        View view = createViewFromTag(name, context, attrs);
        // 自定义View
        if (null == view) {
            view = createView(name, context, attrs);
        }
        skinAttribute.load(view,attrs);
        return view;
    }


    private View createViewFromTag(String name, Context context, AttributeSet attrs) {
        //包含了 . 自定义控件
        if (-1 != name.indexOf(".")) {
            return null;
        }
        View view = null;
        for (int i = 0; i < mClassPrefixList.length; i++) {
            view = createView(mClassPrefixList[i] + name, context, attrs);
            if (null != view) {
                break;
            }
        }
        return view;
    }

    private View createView(String name,Context context,AttributeSet attrs){
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (null == constructor) {
            try {
                Class<? extends View> aClass = context.getClassLoader().loadClass(name).asSubclass
                        (View.class);
                constructor = aClass.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {
            }
        }
        if (null != constructor) {
            try {
                return constructor.newInstance(context, attrs);
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        skinAttribute.applySkin();
    }
}
