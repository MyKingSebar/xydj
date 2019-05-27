package com.bokang.yijia.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.example.yijia.util.log.LatteLogger;
import com.bokang.yijia.ExampleActivity;
import com.google.gson.JsonObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;


public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        final Set<String> keys = bundle.keySet();
        final JSONObject json = new JSONObject();
        for (String key : keys) {
            final Object val = bundle.get(key);
            json.put(key, val);
        }

        LatteLogger.json("PushReceiver", json.toJSONString());

        final String pushAction = intent.getAction();
        LatteLogger.e("PushReceiver","pushAction:"+ pushAction);
        if (pushAction.equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
            //处理接收到的信息
            onReceivedMessage(bundle);
        } else if (pushAction.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            //打开相应的Notification
            onOpenNotification(context, bundle);
        }
    }

    private void onReceivedMessage(Bundle bundle) {
        final String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        final String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        final int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        final String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        final String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        final String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        LatteLogger.e("jialei","extra:"+extra);
        JSONObject object= JSON.parseObject(extra);
        String pushType=object.getString("pushType");
        if(TextUtils.equals("1",pushType)||TextUtils.equals("2",pushType)||TextUtils.equals("3",pushType)||TextUtils.equals("4",pushType)||TextUtils.equals("5",pushType)||TextUtils.equals("6",pushType)||TextUtils.equals("7",pushType)||TextUtils.equals("8",pushType)||TextUtils.equals("9",pushType)){
            final IGlobalCallback<String> callback = CallbackManager
                    .getInstance()
                    .getCallback(CallbackType.REFRESHNOTIFY);
            if (callback != null) {
                callback.executeCallback("");
            }
        }
    }

    private void onOpenNotification(Context context, Bundle bundle) {
        final String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        final Bundle openActivityBundle = new Bundle();
        final Intent intent = new Intent(context, ExampleActivity.class);
        intent.putExtras(openActivityBundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ContextCompat.startActivity(context, intent, null);
    }
}
