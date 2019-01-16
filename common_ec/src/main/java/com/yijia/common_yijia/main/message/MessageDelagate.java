package com.yijia.common_yijia.main.message;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.example.latte.delegates.bottom.BottomItemDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ec.detail.TabPagerAdapter;
import com.example.latte.util.log.LatteLogger;

import butterknife.BindView;
import butterknife.OnClick;


public class MessageDelagate extends BottomItemDelegate {

    @BindView(R2.id.tablayout)
    TabLayout mTabLayout = null;
    @BindView(R2.id.view_pager)
    ViewPager mViewPager = null;

    @OnClick(R2.id.iv_search)
    void search(){
        showToast("2|3:"+((2|3)&(~3)));
        LatteLogger.d("jialei","2|3:"+((2|3)&(~3)));
        Log.d("jialei","2|3:"+((2|3)&(~3)));
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_message;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initTabLayout();
        initPager();
        Log.d("jialei","2|3:"+((2|3)&(~3)));
    }


    private void initTabLayout() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setSelectedTabIndicatorColor
                (ContextCompat.getColor(getContext(), R.color.app_text_orange));
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setupWithViewPager(mViewPager);
    }
    private void initPager() {
        final PagerAdapter adapter = new MessageTabPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter);
    }
}
