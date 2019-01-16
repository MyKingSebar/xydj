package com.yijia.common_yijia.friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.latte.delegates.bottom.BottomItemDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.friends.adapter.MyFriendsAdapter;
import com.yijia.common_yijia.friends.adapter.MyGuardianAdapter;
import com.yijia.common_yijia.friends.bean.FriendsBean;
import com.yijia.common_yijia.friends.bean.GuardianBean;
import com.yijia.common_yijia.friends.presenter.FriendsPresenter;
import com.yijia.common_yijia.friends.view.FriendsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendsDelegate extends BottomItemDelegate implements HeadLayout.OnClickHeadHeadImage, FriendsView {
    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    @BindView(R2.id.guardianship_recycler)
    RecyclerView guardianshipRecycler;
    @BindView(R2.id.doctor_recycler)
    RecyclerView doctorRecycler;
    @BindView(R2.id.friends_recycler)
    RecyclerView friendsRecycler;
    @BindView(R2.id.oldmapuserlist_recycler)
    RecyclerView oldmapuserlistRecycler;
    //亲友团好友列表
    private List<FriendsBean> friendsBeans;
    private FriendsPresenter friendsPresenter;
    //监护人列表
    private List<GuardianBean> guardianBeans;

    @Override
    public Object setLayout() {
        return R.layout.delegate_friends;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initFriendsHead();
        friendsBeans = new ArrayList<>();
        guardianBeans = new ArrayList<>();
        friendsPresenter = new FriendsPresenter(this);
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        friendsPresenter.reqFriendData(token);
        friendsPresenter.reqGuardianData(token);
    }

    private void initFriendsHead() {
        //初始化头布局
        headLayout.setHeadName("亲友团", "#FDBA63", 18);
        headLayout.setHeadlayoutBagColor("#ffffff");
        headLayout.setRightHeadImage(R.mipmap.tianjiafriend, true);
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

    @Override
    public void respFriendsError(String error) {
        Toast.makeText(_mActivity, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void respFriendsSuccess(JSONArray friends) {
        if (friends == null) {
            //todo 没有监护人的处理
            return;
        }
        for (int i = 0; i < friends.size(); i++) {
            final JSONObject jsonObject = friends.getJSONObject(i);
            final String friendUserId = jsonObject.getString("friendUserId");
            final String nickname = jsonObject.getString("nickname");
            final String realName = jsonObject.getString("realName");
            final String userStatus = jsonObject.getString("userStatus");
            final String userHead = jsonObject.getString("userHead");
            final String rongCloudToken = jsonObject.getString("rongCloudToken");
            FriendsBean friendsBean = new FriendsBean(friendUserId, nickname, realName, userStatus, userHead, rongCloudToken);
            friendsBeans.add(friendsBean);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        friendsRecycler.setLayoutManager(layoutManager);
        MyFriendsAdapter adapter = new MyFriendsAdapter(R.layout.friends_itme, friendsBeans);
        friendsRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //todo 好友列表点击事件 跳转会话页面
            }
        });
    }

    @Override
    public void respGuardianSuccess(JSONArray guardianUserList, JSONArray oldMapUserList) {
        if (guardianUserList == null && oldMapUserList == null){
            //todo 没有监护人和被监护人时候的处理。
            return;
        }

        for (int i = 0; i < guardianUserList.size(); i++) {
            final JSONObject jsonObject = guardianUserList.getJSONObject(i);
            final String userId = jsonObject.getString("userId");
            final String realName = jsonObject.getString("realName");
            final String nickname = jsonObject.getString("nickname");
            final String headImage = jsonObject.getString("headImage");
            final String isMain = jsonObject.getString("isMain");
            final String rongCloudToken = jsonObject.getString("rongCloudToken");
            GuardianBean guardianBean = new GuardianBean(userId, realName, nickname, headImage, isMain, rongCloudToken);
            guardianBeans.add(guardianBean);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        guardianshipRecycler.setLayoutManager(layoutManager);
        MyGuardianAdapter adapter = new MyGuardianAdapter(R.layout.guardian_itme, guardianBeans);
        guardianshipRecycler.setAdapter(adapter);
    }

    @Override
    public void respGuardianError(String guardianError) {
        Log.e("yangfan", "respGuardianError: "+ guardianError);
        Toast.makeText(_mActivity, guardianError, Toast.LENGTH_SHORT).show();
    }
}
