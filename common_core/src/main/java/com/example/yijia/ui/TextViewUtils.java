package com.example.yijia.ui;

import android.support.v4.text.PrecomputedTextCompat;
import android.support.v7.widget.AppCompatTextView;

import java.util.concurrent.Future;

/**
 * @author JiaLei
 * Created on 2019/4/19 11:05
 * E-Mail Address：15033111957@163.com
 */
public class TextViewUtils {
    public static void AppCompatTextViewSetText(AppCompatTextView tv,String text){
        Future<PrecomputedTextCompat> future = PrecomputedTextCompat.getTextFuture(
                text, tv.getTextMetricsParamsCompat(), null);
        tv.setTextFuture(future);
    }
}
