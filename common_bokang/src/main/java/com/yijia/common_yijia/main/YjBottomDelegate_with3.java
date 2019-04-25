package com.yijia.common_yijia.main;

import android.graphics.Color;

import com.example.latte.ec.R;
import com.example.yijia.delegates.bottom.BaseBottomDelegate_with3;
import com.example.yijia.delegates.bottom.BottomItemDelegate;
import com.example.yijia.delegates.bottom.BottomTabBean;
import com.example.yijia.delegates.bottom.ItemBuilder;
import com.yijia.common_yijia.main.find.FindDelegate;
import com.yijia.common_yijia.main.index.YjIndexDelegate;
import com.yijia.common_yijia.main.index.friendcircle.smallvideo.SmallCameraLisener;
import com.yijia.common_yijia.main.robot.robotmain.RobotMainDelegate;

import java.util.LinkedHashMap;

public class YjBottomDelegate_with3 extends BaseBottomDelegate_with3 {
    SmallCameraLisener mSmallCameraLisener = null;

    public void setmSmallCameraLisener(SmallCameraLisener mSmallCameraLisener) {
        this.mSmallCameraLisener = mSmallCameraLisener;
    }

    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();
        YjIndexDelegate mYjIndexDelegate = new YjIndexDelegate();
        if (mSmallCameraLisener != null) {
            mYjIndexDelegate.setSmallCameraLisener(mSmallCameraLisener);
        }
        items.put(new BottomTabBean(null, "壹家", R.mipmap.icon_button3_home, R.mipmap.icon_button3_home_c), mYjIndexDelegate);
        items.put(new BottomTabBean(null, "", R.mipmap.icon_button3_robot, R.mipmap.icon_button3_robot_c), new RobotMainDelegate());
        items.put(new BottomTabBean(null, "发现", R.mipmap.icon_button3_find, R.mipmap.icon_button3_find_c), new FindDelegate());
        return builder.addItems(items).build();
    }

    @Override
    public int setIndexDelegate() {
        return 0;
    }

    @Override
    public int setClickedColor() {
        return Color.parseColor("#FDBA63");
    }
}
