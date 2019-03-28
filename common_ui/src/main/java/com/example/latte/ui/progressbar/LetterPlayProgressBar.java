package com.example.latte.ui.progressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.latte.ui.R;
import com.example.yijia.ui.roundprogressbar.RxRoundProgressBar;

public class LetterPlayProgressBar extends LinearLayout{
    private View inflate;
    private AppCompatTextView tvPlay;
    private RxRoundProgressBar rxRound;
    private Message mMessage=null;
    private LetterPlayProgressBarPlayListener mLetterPlayProgressBarPlayListener=null;
    private boolean isPlay=false;

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public void setmLetterPlayProgressBarPlayListener(LetterPlayProgressBarPlayListener mLetterPlayProgressBarPlayListener) {
        this.mLetterPlayProgressBarPlayListener = mLetterPlayProgressBarPlayListener;
    }

    public LetterPlayProgressBar(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public LetterPlayProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public LetterPlayProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }
    @SuppressLint("HandlerLeak")
    Handler mRxRoundPdHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rxRound.setProgress((float) msg.obj);
        }
    };
    public void setProgress(float progress){
        mMessage=new Message();
        mMessage.obj=progress;
        mRxRoundPdHandler.sendMessage(mMessage);
    }
    //加载布局
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        inflate = View.inflate(context, R.layout.jview_letterplayprogressbar, this);
        tvPlay = findViewById(R.id.tv_play);
        rxRound = findViewById(R.id.rx_round_pd);
        tvPlay.setOnClickListener(v -> {
            if(isPlay){
                if(mLetterPlayProgressBarPlayListener!=null){
                    mLetterPlayProgressBarPlayListener.letterStop();
                    isPlay=false;
                    tvPlay.setBackgroundResource(R.mipmap.button_letter_pause);
                }
            }else {
                if(mLetterPlayProgressBarPlayListener!=null){
                    mLetterPlayProgressBarPlayListener.letterPlay();
                    isPlay=true;
                    tvPlay.setBackgroundResource(R.mipmap.button_letter_play);
                }
            }
        });
    }



}
