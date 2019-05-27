package com.example.latte.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.latte.ui.R;

/**
 * Created by wangpanfeng@bokangzhixin.com on 2019/5/16.
 */
public class RobotImageView extends RelativeLayout {
    private Context context;
    private ImageView userImageView, robotImageView;
    private int width, height;

    public RobotImageView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public RobotImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public RobotImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        inflate(context, R.layout.robot_image_view, this);
        userImageView = findViewById(R.id.user_image);
        robotImageView = findViewById(R.id.robot_image);
        robotImageView.setImageResource(R.mipmap.robot_moment_list);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        ViewGroup.LayoutParams params = getLayoutParams();
//        params.width += 12;
//
//        params.height += 4;

    }

    public ImageView userImageView() {
        return userImageView;
    }
    public ImageView robotImageView() {
        return robotImageView;
    }

    public void setRobotOnline(boolean online) {
        if (online) {
            robotImageView.setImageResource(R.mipmap.robot_moment_list);
        } else {
            robotImageView.setImageResource(R.mipmap.robot_moment_list);
        }
    }

    public void setRobotOnline(int online) {
        switch (online) {
            case 1:
                robotImageView.setImageResource(R.mipmap.robot_moment_list);
                break;
            case 2:
                robotImageView.setImageResource(R.mipmap.robot_moment_list);
                break;
            default:
        }
    }

}
