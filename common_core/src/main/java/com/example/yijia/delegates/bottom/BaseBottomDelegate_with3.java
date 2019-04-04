package com.example.yijia.delegates.bottom;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.latte.R;
import com.example.latte.R2;
import com.example.yijia.delegates.LatteDelegate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import me.yokeyword.fragmentation.ISupportFragment;

public abstract class BaseBottomDelegate_with3 extends LatteDelegate implements View.OnClickListener {

    private final ArrayList<BottomTabBean> TAB_BEANS = new ArrayList<>();
    private final ArrayList<BottomItemDelegate> ITEM_DELEGATES = new ArrayList<>();
    private final LinkedHashMap<BottomTabBean, BottomItemDelegate> ITEMS = new LinkedHashMap<>();
    private int mCurrentDelegate = 0;
    private int mIndexDelegate = 0;
    private int mClickedColor = Color.RED;

    @BindView(R2.id.bottom_bar)
    LinearLayoutCompat mBottomBar = null;


    public abstract LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder);

    @Override
    public Object setLayout() {
        return R.layout.delegate_bottom_with3;
    }

    public abstract int setIndexDelegate();

    @ColorInt
    public abstract int setClickedColor();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndexDelegate = setIndexDelegate();
        if (setClickedColor() != 0) {
            mClickedColor = setClickedColor();
        }

        final ItemBuilder builder = ItemBuilder.builder();
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = setItems(builder);
        ITEMS.putAll(items);
        for (Map.Entry<BottomTabBean, BottomItemDelegate> item : ITEMS.entrySet()) {
            final BottomTabBean key = item.getKey();
            final BottomItemDelegate value = item.getValue();
            TAB_BEANS.add(key);
            ITEM_DELEGATES.add(value);

        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        final int size = ITEMS.size();
        for (int i = 0; i < size; i++) {
            LayoutInflater.from(getContext()).inflate(R.layout.bottom_item_image_layout, mBottomBar);
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            //设置每个item的点击事件
            item.setTag(i);
            item.setOnClickListener(this);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(0);
            final BottomTabBean bean = TAB_BEANS.get(i);

            final CharSequence mIcon = bean.getIcon();
            final int mIconId = bean.getIconId();
            final int mIconcId = bean.getIconclickedid();
            //初始化数据
            if (mIconId != 0) {
                itemTitle.setBackgroundResource(mIconId);
            }
            if (i == mIndexDelegate) {
                if (mIconcId != 0) {
                    itemTitle.setBackgroundResource(mIconcId);
                }
            }

        }

        final ISupportFragment[] delegateArray = ITEM_DELEGATES.toArray(new ISupportFragment[size]);
        getSupportDelegate().loadMultipleRootFragment(R.id.bottom_bar_delegate_container, mIndexDelegate, delegateArray);
    }

    private void resetColor() {
        final int count = mBottomBar.getChildCount();
        for (int i = 0; i < count; i++) {
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(0);

            final int mIconId = TAB_BEANS.get(i).getIconId();
            if (mIconId != 0) {
                itemTitle.setBackgroundResource(mIconId);
            }

        }
    }

    @Override
    public void onClick(View v) {
        final int tag = (int) v.getTag();
        resetColor();
        final RelativeLayout item = (RelativeLayout) v;
        final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(0);

        final CharSequence mIcon = TAB_BEANS.get(tag).getIcon();
        final int mIconcId = TAB_BEANS.get(tag).getIconclickedid();
        if (mIcon != null) {
            itemTitle.setTextColor(mClickedColor);
        }
        if (mIconcId != 0) {
            itemTitle.setBackgroundResource(mIconcId);
        }


        getSupportDelegate().showHideFragment(ITEM_DELEGATES.get(tag), ITEM_DELEGATES.get(mCurrentDelegate));
        //一定要注意先后顺序
        mCurrentDelegate = tag;
    }
}
