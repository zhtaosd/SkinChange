package com.dongnao.dnskin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.longfor.skin.core3.SkinManager;


/**
 * @author Lance
 * @date 2018/3/12
 */

public class SkinActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
    }

    public void change(View view) {
        //换肤
        SkinManager.getInstance().loadSkin("/sdcard/app-skin-debug.skin");
    }

    public void restore(View view) {
        SkinManager.getInstance().loadSkin(null);
    }
}
