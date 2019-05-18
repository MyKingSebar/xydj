package com.example.yijia.util.listener;

import android.view.View;

/**
 * Created by wangpanfeng@bokangzhixin.com on 2019/5/18.
 */
public abstract class OnSingleClickListener implements View.OnClickListener {
    private long mLastClickTime;
    private long timeInterval = 1000L;

    public OnSingleClickListener() {

    }

    public OnSingleClickListener(long interval) {
        this.timeInterval = interval;
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) {
            onSingleClick(v);
            mLastClickTime = nowTime;
        } else {
            onFastClick();
        }
    }

    protected abstract void onSingleClick(View v);
    protected void onFastClick(){};
}