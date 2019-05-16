package com.example.latte.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.latte.ui.R;

/**
 * Created by wangpanfeng@bokangzhixin.com on 2019/5/16.
 */
public class RobotImageView extends RelativeLayout {
    private Context context;
    private ImageView userImageView, robotImageView;


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
    }

    public ImageView userImageView() {
        return userImageView;
    }

    public void setRobotOnline(boolean online) {
        if(online) {
            robotImageView.setImageResource(R.mipmap.robot_moment_list);
        } else {
            robotImageView.setImageResource(R.mipmap.robot_moment_list);
        }
    }

}
