package com.example.yijia.ui.sms;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

public enum JSmsUtil {
    //instance
    INSTENCE;
    private void JSmsUtil(){

    }
    /**
     * 调用系统界面，给指定的号码发送短信，并附带短信内容
     *
     * @param context
     * @param number
     * @param body
     */
    public  void sendSmsWithBody(@NonNull Context context, String number, String body) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + number));
        sendIntent.putExtra("sms_body", body);
        context.startActivity(sendIntent);
    }
}
