package com.yijia.common_yijia.main.find.healthself;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.baidu.ocr.ui.util.DimensionUtil;
import com.example.latte.ec.R;

/**
 * Created by wangpanfeng@bokangzhixin.com on 2019/5/25.
 */
public class CameraScanView extends FrameLayout {

    private Context context;
    private FrameLayout bg;
    private View line;
    private ValueAnimator anim;

    public CameraScanView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CameraScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CameraScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {

        inflate(context, R.layout.view_camera_scan, this);
        bg = findViewById(R.id.bg);
        line = findViewById(R.id.line);

        anim = ValueAnimator.ofInt(DimensionUtil.dpToPx(10), DimensionUtil.dpToPx(180));
        anim.setDuration(18000);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                bg.setPadding(getPaddingLeft(), currentValue, getPaddingRight(), getPaddingBottom());
            }
        });
        anim.start();

    }

    public void pause() {
        anim.pause();
    }

    public void resume() {
        anim.resume();
    }

    public void release() {
        anim.cancel();
    }
}
