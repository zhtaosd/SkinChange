package com.longfor.skin.core3;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.longfor.skin.core3.util.SkinResource;
import com.longfor.skin.core3.util.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

/**
* view的属性处理类
* @author zhanghaitao
* created at 2018/10/22  16:08
*/
public class SkinAttribute {
    private static final List<String> mAttributes = new ArrayList<>();
    List<SkinView> mSkinViews = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");

        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");

        mAttributes.add("skinTypeface");
    }

    private Typeface typeface;

    public SkinAttribute(Typeface typeface) {
        this.typeface = typeface;
    }

    public void load(View view, AttributeSet attrs){
        List<SkinPair> skinPairs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);
            if(mAttributes.contains(attributeName)){
                String attrbuteValue = attrs.getAttributeValue(i);
                if(attrbuteValue.contains("#")){
                    continue;
                }
                int resID ;
                if(attributeName.startsWith("?")){
                    int attrID = Integer.parseInt(attrbuteValue.substring(1));
                    resID = SkinThemeUtils.getResID(view.getContext(),new int[]{attrID})[0];
                }else{
                    resID = Integer.parseInt(attrbuteValue.substring(1));
                }
                if(resID!=0){
                    SkinPair skinPair = new SkinPair(attributeName,resID);
                    skinPairs.add(skinPair);
                }
            }
        }
        if(!skinPairs.isEmpty()||view instanceof TextView){
            SkinView skinView = new SkinView(view,skinPairs);
            skinView.applySkinTypeface(typeface);
        }
    }

    public void applySkin(Typeface typeface){
        for (SkinView mSkinView : mSkinViews) {
            mSkinView.applySkin(typeface);
        }
    }

    //用来存储layout信息
    static class SkinView{
        View view;
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        public void applySkin(Typeface typeface){
            applySkinTypeface(typeface);
            for (SkinPair skinPair : skinPairs) {
                Drawable left = null,top = null,right = null,bottom = null;
                switch (skinPair.attributeName){
                    case"background":
                        Object background = SkinResource.getInstance().getBackground(skinPair.resID);
                        if(background instanceof Integer){
                            view.setBackgroundColor((Integer) background);
                        }else{
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case"src":
                        background = SkinResource.getInstance().getBackground(skinPair.resID);
                        if(background instanceof Integer){
                            ((ImageView)view).setImageDrawable(new ColorDrawable((Integer) background));
                        }else{
                            ((ImageView)view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case"textColor":
                        ((TextView)view).setTextColor(SkinResource.getInstance().getColorStateList(skinPair.resID));
                        break;
                    case"drawableLeft":
                        left = SkinResource.getInstance().getDrawble(skinPair.resID);
                        break;
                    case"drawableTop":
                        top = SkinResource.getInstance().getDrawble(skinPair.resID);
                        break;
                    case"drawableRight":
                        right = SkinResource.getInstance().getDrawble(skinPair.resID);
                        break;
                    case"drawableBottom":
                        bottom = SkinResource.getInstance().getDrawble(skinPair.resID);
                        break;
                    case"skinTypeface":
                        Typeface typeface1 = SkinResource.getInstance().getTypeface(skinPair.resID);
                        applySkinTypeface(typeface1);
                        break;
                }
                if(null!=left || null!=top ||null!=right ||null!=bottom ){
                    ((TextView)view).setCompoundDrawablesRelativeWithIntrinsicBounds(left,top,right,bottom);
                }
            }
        }
        private void applySkinTypeface(Typeface typeface){
            if(view instanceof TextView){
                ((TextView)view).setTypeface(typeface);
            }
        }
    }
    //存储属性集合
    static class SkinPair{
        String attributeName;
        int resID;

        public SkinPair(String attributeName, int resID) {
            this.attributeName = attributeName;
            this.resID = resID;
        }
    }
}
