package com.yijia.common_yijia.sign;

import android.graphics.Color;

import com.example.latte.delegates.bottom.BaseBottomDelegate;
import com.example.latte.delegates.bottom.BottomItemDelegate;
import com.example.latte.delegates.bottom.BottomTabBean;
import com.example.latte.delegates.bottom.ItemBuilder;
import com.example.latte.ec.R;
import com.example.latte.ec.main.cart.ShopCartDelegate;
import com.example.latte.ec.main.discover.DiscoverDelegate;
import com.example.latte.ec.main.index.IndexDelegate;
import com.example.latte.ec.main.sort.SortDelegate;
import com.yijia.common_yijia.main.index.YjIndexDelegate;

import java.util.LinkedHashMap;

public class YjBottomDelegate extends BaseBottomDelegate {


    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();
        items.put(new BottomTabBean(null, "首页", R.mipmap.icon_shouye, R.mipmap.icon_shouye_c), new YjIndexDelegate());
        items.put(new BottomTabBean(null, "远程看护", R.mipmap.icon_kanhu, R.mipmap.icon_kanhu_c), new SortDelegate());
        items.put(new BottomTabBean(null, "消息", R.mipmap.icon_xiaoxi,  R.mipmap.icon_xiaoxi_c), new DiscoverDelegate());
        items.put(new BottomTabBean(null, "我的", R.mipmap.icon_wode, R.mipmap.icon_wode_c), new ShopCartDelegate());
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
