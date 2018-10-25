package com.longfor.skin.core3.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
* 用来进行资源获取的工具类
* @author zhanghaitao
* created at 2018/10/23  11:00
*/
public class SkinResource {
    private static SkinResource instance;

    private Resources mAppResources;
    private Resources mSkinResource;
    private String mSkinPkgName;

    private boolean isDefaultSkin = true;

    private SkinResource(Context context) {
        mAppResources = context.getResources();
    }
    public static SkinResource getInstance(){
        return  instance;
    }

    public static void  init(Context context){
        synchronized (SkinResource.class){
            if(instance == null){
                instance = new SkinResource(context);
            }
        }
    }

    //获取View的背景
    public Object getBackground(int resID){
        String resourceTypeName = mAppResources.getResourceTypeName(resID);
        if(resourceTypeName.equals("color")){
            return getColor(resID);
        }else{
            return getDrawble(resID);
        }
    }

    public Drawable getDrawble(int resID) {
        if(isDefaultSkin){
            return mAppResources.getDrawable(resID);
        }
        int skinID = getIdentifier(resID);
        if(skinID == 0){
            return mAppResources.getDrawable(resID);
        }
        return mSkinResource.getDrawable(resID);
    }

    public int getColor(int resID) {
        if(isDefaultSkin){
            return mAppResources.getColor(resID);
        }
        int skinID = getIdentifier(resID);
        if(skinID == 0){
            return mAppResources.getColor(resID);
        }
        return mSkinResource.getColor(skinID);
    }

    //获取要加载资源的ID
    private int getIdentifier(int resID) {
        if(isDefaultSkin){
            return resID;
        }
        String resName = mAppResources.getResourceEntryName(resID);
        String resType = mAppResources.getResourceTypeName(resID);
        int skinID = mSkinResource.getIdentifier(resName,resType,mSkinPkgName);
        return skinID;
    }

    //获取颜色
    public ColorStateList getColorStateList(int resID){
        if(isDefaultSkin){
            return mAppResources.getColorStateList(resID);
        }
        int skinID = getIdentifier(resID);
        if(skinID == 0){
            return mAppResources.getColorStateList(resID);
        }
        return mSkinResource.getColorStateList(resID);
    }

    public void applySkin(Resources resources,String pkgName){
        mSkinResource = resources;
        mSkinPkgName = pkgName;
        isDefaultSkin = TextUtils.isEmpty(pkgName)||resources == null;
    }

    public void reset(){
        isDefaultSkin = true;
        mSkinPkgName = "";
        mSkinResource = null;
    }

    public Typeface getTypeface(int skinTypeceID) {
        String skinTypefacePath = getString(skinTypeceID);
        if(TextUtils.isEmpty(skinTypefacePath)){
            return Typeface.DEFAULT;
        }
        Typeface typeface;
        if(isDefaultSkin){
            typeface = Typeface.createFromAsset(mAppResources.getAssets(),skinTypefacePath);
        }else{
            typeface = Typeface.createFromAsset(mSkinResource.getAssets(),skinTypefacePath);
        }
        return typeface;
    }

    public String getString(int resID){
        if(isDefaultSkin){
            return mAppResources.getString(resID);
        }
        int skinID  = getIdentifier(resID);
        if(skinID == 0){
            return mAppResources.getString(resID);
        }else{
            return  mSkinResource.getString(resID);
        }
    }
}
