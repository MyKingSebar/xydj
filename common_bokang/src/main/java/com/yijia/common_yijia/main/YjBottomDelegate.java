//package com.yijia.common_yijia.main;
//
//import android.graphics.Color;
//
//import com.example.latte.ec.R;
//import com.example.yijia.delegates.bottom.BaseBottomDelegate;
//import com.example.yijia.delegates.bottom.BottomItemDelegate;
//import com.example.yijia.delegates.bottom.BottomTabBean;
//import com.example.yijia.delegates.bottom.ItemBuilder;
//import com.yijia.common_yijia.main.index.YjIndexDelegate;
//import com.yijia.common_yijia.main.index.friendcircle.smallvideo.SmallCameraLisener;
//import com.yijia.common_yijia.main.message.MessageDelagate;
//import com.yijia.common_yijia.main.mine.MineDelegate;
//
//import java.util.LinkedHashMap;
//
//public class YjBottomDelegate extends BaseBottomDelegate {
//    SmallCameraLisener mSmallCameraLisener=null;
//
//    public void setmSmallCameraLisener(SmallCameraLisener mSmallCameraLisener) {
//        this.mSmallCameraLisener = mSmallCameraLisener;
//    }
//
//    @Override
//    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
//        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();
//        YjIndexDelegate mYjIndexDelegate= new YjIndexDelegate();
//        if(mSmallCameraLisener!=null){
//
//            mYjIndexDelegate.setSmallCameraLisener(mSmallCameraLisener);
//        }
//        items.put(new BottomTabBean(null, "壹家", R.mipmap.icon_shouye, R.mipmap.icon_shouye_c),mYjIndexDelegate );
//        items.put(new BottomTabBean(null, "消息", R.mipmap.icon_xiaoxi,  R.mipmap.icon_xiaoxi_c), new MessageDelagate());
////        items.put(new BottomTabBean(null, "亲友团", R.mipmap.icon_kanhu, R.mipmap.icon_kanhu_c), new FriendsDelegate());
//        items.put(new BottomTabBean(null, "我的", R.mipmap.icon_wode, R.mipmap.icon_wode_c), new MineDelegate());
//        return builder.addItems(items).build();
//    }
//
//    @Override
//    public int setIndexDelegate() {
//        return 0;
//    }
//
//    @Override
//    public int setClickedColor() {
//        return Color.parseColor("#FDBA63");
//    }
//}
