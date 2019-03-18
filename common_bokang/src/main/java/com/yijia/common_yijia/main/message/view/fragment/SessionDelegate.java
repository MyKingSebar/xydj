package com.yijia.common_yijia.main.message.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import android.view.View;


import com.example.yijia.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.yijia.common_yijia.main.message.presenter.SessionPresenter;
import com.yijia.common_yijia.main.message.view.iview.IViewSession;

import butterknife.BindView;


/**
 * 对话页面
 */
public class SessionDelegate extends LatteDelegate implements IViewSession {


    @BindView(R2.id.rv_image_container)
    RecyclerView rvImageContainer;

    private SessionPresenter sessionPresenter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_recycleview;
    }

    private void initPresenter() {
         sessionPresenter = new SessionPresenter(this);
         sessionPresenter.reqSessionlist();
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        //初始化
        initPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sessionPresenter.onDestroy();
    }
}
