package com.yijia.common_yijia.main.robot;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.delegates.bottom.BottomItemDelegate;
import com.yijia.common_yijia.main.message.MessageTabPagerAdapter;
import com.yijia.common_yijia.main.message.trtc.session.SessionFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class RobotMainDelegate extends BottomItemDelegate {
    @BindView(R2.id.tv_save)
    AppCompatTextView tvSave;
    @BindView(R2.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R2.id.tv_back)
    RelativeLayout tvBack;

    @BindView(R2.id.tablayout)
    TabLayout mTabLayout = null;
    @BindView(R2.id.view_pager)
    ViewPager mViewPager = null;


    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_main;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initHead();
        initTabLayout();
        initPager();
    }

    //初始化头布局
    private void initHead() {
        tvTitle.setText("机器人");
        tvSave.setVisibility(View.GONE);
        tvBack.setVisibility(View.GONE);
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
