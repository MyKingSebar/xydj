package com.example.yijia.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.PrecomputedTextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.example.latte.R;

import java.util.Objects;
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
    public static int getColor(Context mCOntext,int mRcolor){
        return ContextCompat.getColor(Objects.requireNonNull(mCOntext), mRcolor);
    }
    public static Drawable getDrawable(Context mCOntext, int mRid){
        return ContextCompat.getDrawable(Objects.requireNonNull(mCOntext), mRid);
    }
    public  static  void  setBackground(Context mCOntext,View view,int drawable){
        view.setBackground(getDrawable(mCOntext,drawable));
    }
}
