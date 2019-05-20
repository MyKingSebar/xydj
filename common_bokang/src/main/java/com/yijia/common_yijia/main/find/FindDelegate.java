package com.yijia.common_yijia.main.find;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.delegates.bottom.BottomItemDelegate;
import com.yijia.common_yijia.main.find.healthself.HealthMainDelegate;
import com.yijia.common_yijia.main.find.homedoc.HomeDoctorDelegate;
import com.yijia.common_yijia.main.message.trtc.session.SessionFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class FindDelegate extends BottomItemDelegate {
    @BindView(R2.id.tv_save)
    AppCompatTextView tvSave;
    @BindView(R2.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R2.id.tv_back)
    RelativeLayout tvBack;

    @BindView(R2.id.ll_friends)
    LinearLayoutCompat ll_friends;
    @BindView(R2.id.ll_homedoc)
    LinearLayoutCompat ll_homedoc;
    @BindView(R2.id.ll_door)
    LinearLayoutCompat ll_door;
    @BindView(R2.id.ll_health)
    LinearLayoutCompat ll_health;

    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.ll_friends)
    void ll_friends() {
        getParentDelegate().getSupportDelegate().start(new  SessionFragment());
    }
    @OnClick(R2.id.ll_homedoc)
    void ll_homedoc() {
        getParentDelegate().getSupportDelegate().start(new HomeDoctorDelegate());
    }
    @OnClick(R2.id.ll_door)
    void ll_door() {


    }
    @OnClick(R2.id.ll_health)
    void ll_health() {
        getParentDelegate().getSupportDelegate().start(new HealthMainDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_find;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initHead();
    }

    //初始化头布局
    private void initHead() {
        tvTitle.setText("发现");
        tvSave.setVisibility(View.GONE);
        tvBack.setVisibility(View.GONE);
    }




}
