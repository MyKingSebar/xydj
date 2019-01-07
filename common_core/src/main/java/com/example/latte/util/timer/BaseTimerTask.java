package com.example.latte.util.timer;

import java.util.TimerTask;

public class BaseTimerTask extends TimerTask {

    private ITimeListener mITimeListener = null;

    public BaseTimerTask(ITimeListener timeListener) {
        this.mITimeListener = timeListener;
    }


    @Override
    public void run() {
        if (mITimeListener != null) {
            mITimeListener.onTimer();

        }
    }


}
