package com.bokang.yijia.mobshare.listener;

import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class MyPlatformActionListener implements PlatformActionListener {
    private Context context=null;
    public MyPlatformActionListener(Context context){
        this.context=context;
    }
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Toast.makeText(context, "Share Complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        final String error = throwable.toString();
        Toast.makeText(context, "Share Failure" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Toast.makeText(context, "Cancel Share", Toast.LENGTH_SHORT).show();
    }
}