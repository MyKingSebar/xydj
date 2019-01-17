package com.yijia.common_yijia.main.friends.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class AddFriendsDelegate extends LatteDelegate implements HeadLayout.OnClickHeadReturn {
    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    @BindView(R2.id.search_friends)
    LinearLayout searchFriends;
    @BindView(R2.id.sweep_relative)
    RelativeLayout sweepRelative;

    @Override
    public Object setLayout() {
        return R.layout.delegate_addfriends;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        headLayout.setHeadName("添加亲友", "#FDBA63", 18);
        headLayout.setHeadlayoutBagColor("#ffffff");
        headLayout.setHeadleftImg(true, R.mipmap.fanhui);
        headLayout.setOnClickHeadReturn(this);
    }

    @Override
    public void onClickHeadReturn() {
        getSupportDelegate().pop();
    }

    @OnClick({R2.id.search_friends, R2.id.sweep_relative})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.search_friends) {
            getSupportDelegate().start(new SearchFriendsDelegate());
        } else if (i == R.id.sweep_relative) {
            Toast.makeText(_mActivity, "点击扫一扫加好友", Toast.LENGTH_SHORT).show();
        }
    }
}
