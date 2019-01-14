package com.yijia.common_yijia.friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.latte.delegates.bottom.BottomItemDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;
import com.yijia.common_yijia.friends.adapter.MyFriendsAdapter;
import com.yijia.common_yijia.friends.presenter.FriendsPresenter;
import com.yijia.common_yijia.friends.view.FriendsView;

import java.util.ArrayList;

import butterknife.BindView;

public class FriendsDelegate extends BottomItemDelegate implements HeadLayout.OnClickHeadHeadImage, FriendsView {
    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    @BindView(R2.id.guardianship_recycler)
    RecyclerView guardianshipRecycler;
    @BindView(R2.id.doctor_recycler)
    RecyclerView doctorRecycler;
    @BindView(R2.id.friends_recycler)
    RecyclerView friendsRecycler;
    private ArrayList<String> strings;
    private FriendsPresenter friendsPresenter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_friends;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initFriendsHead();

        friendsPresenter = new FriendsPresenter(this);


        strings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            strings.add("itme" + i);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        friendsRecycler.setLayoutManager(layoutManager);
        MyFriendsAdapter adapter = new MyFriendsAdapter(R.layout.friends_itme, strings);
        friendsRecycler.setAdapter(adapter);

    }

    private void initFriendsHead() {
        //初始化头布局
        headLayout.setHeadName("亲友团", "#FDBA63", 18);
        headLayout.setHeadlayoutBagColor("#ffffff");
        headLayout.setRightHeadImage(R.mipmap.fanhui, true);
        headLayout.setOnClickHeadRightImage(this);
    }

    @Override
    public void onClickHeadHeadImage() {
        Toast.makeText(_mActivity, "111111", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        friendsPresenter.onDestroy();
    }
}
