package com.yijia.common_yijia.main.message.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.yijia.common_yijia.main.message.presenter.NoticePresenter;
import com.yijia.common_yijia.main.message.view.iview.NoticeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 通知页面
 */
public class NoticeDelegate extends LatteDelegate implements NoticeView {
    @BindView(R2.id.notice_recycler)
    RecyclerView noticeRecycler;
    private NoticePresenter notice_presenter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_notice;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initRecycler();
    }

    private void initRecycler() {
        notice_presenter = new NoticePresenter(this);
        notice_presenter.reqNoticeData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        notice_presenter.onDestroy();
    }
}
