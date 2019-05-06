package com.yijia.common_yijia.main.friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common_tencent_tuikit.Constants;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.ui.dialog.RxDialogSureCancelListener;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.adapter.MyFriendsAdapter;
import com.yijia.common_yijia.main.friends.adapter.MyGuardianAdapter;
import com.yijia.common_yijia.main.friends.bean.FriendsBean;
import com.yijia.common_yijia.main.friends.bean.GuardianBean;
import com.yijia.common_yijia.main.friends.presenter.FriendsPresenter;
import com.yijia.common_yijia.main.friends.view.fragment.AddFriendsDelegate;
import com.yijia.common_yijia.main.friends.view.iview.FriendsView;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragment2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 亲友团列表
 */

public class FriendsDelegate extends LatteDelegate implements HeadLayout.OnClickHeadHeadImage, FriendsView ,CommonClickListener{
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
    @BindView(R2.id.no_guardian)
    TextView noGuardian;
    @BindView(R2.id.no_doctor)
    TextView noDoctor;
    @BindView(R2.id.no_friends)
    TextView noFriends;
    //亲友团好友列表
    private List<FriendsBean> friendsBeans;
    private FriendsPresenter friendsPresenter;
    //监护人列表
    private List<GuardianBean> guardianBeans;

    PersonalChatFragment2 mCurrentFragment=null;
    String token=null;
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
        token= YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        friendsPresenter.reqFriendData(token);
//        friendsPresenter.reqGuardianData(token);
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
        // 点击添加好友图片跳转添加好友页面
        getParentDelegate().getSupportDelegate().start(new AddFriendsDelegate());
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
        if (friends.size() == 0) {
            // 没有好友的处理
            noFriends.setVisibility(View.VISIBLE);
            noFriends.setOnClickListener(v -> {
                // 没有好友点击 跳转添加好友的操作
                getParentDelegate().getSupportDelegate().start(new AddFriendsDelegate());
            });
            return;
        }
        //有好友的处理
        noFriends.setVisibility(View.GONE);
        friendsBeans.clear();
        for (int i = 0; i < friends.size(); i++) {
            final JSONObject jsonObject = friends.getJSONObject(i);
            Log.e("qqqq", "respFriendsSuccess: "+jsonObject.toJSONString() );
            final long friendUserId = jsonObject.getLong("friendUserId");
            final String nickname = jsonObject.getString("nickname");
            final String realName = jsonObject.getString("realName");
            final String userStatus = jsonObject.getString("userStatus");
            final String userHead = jsonObject.getString("userHead");
            final String identifier = jsonObject.getString("tencentImUserId");
            FriendsBean friendsBean = new FriendsBean(friendUserId, nickname, realName, userStatus, userHead, identifier);
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
        adapter.setmCommonClickListener(this);
        friendsRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            String identifier = friendsBeans.get(position).getIdentifier();
            String nickname = friendsBeans.get(position).getNickname();
            Bundle mArgs = new Bundle();
            mArgs.putString(Constants.INTENT_DATA, identifier);
            mCurrentFragment = new PersonalChatFragment2();
            mCurrentFragment.setArguments(mArgs);
            getParentDelegate().getSupportDelegate().start(mCurrentFragment);

//                ConversationTencentDelegate delegate=new ConversationTencentDelegate();
//                 Bundle mArgs = new Bundle();
//                mArgs.putString("identifier",identifier);
//                mArgs.putString("nickname",nickname);
//                delegate.setArguments(mArgs);
//                 getParentDelegate().getSupportDelegate().start(delegate);
        });
    }

    @Override
    public void respGuardianSuccess(JSONArray guardianUserList, JSONArray oldMapUserList) {
        if (guardianUserList.size() == 0 && oldMapUserList.size() == 0) {
            //没有监护人和被监护人时候的处理。
            noGuardian.setVisibility(View.VISIBLE);
            noGuardian.setOnClickListener(v -> {
                //todo 没有监护人 跳转绑定监护人的操作
            });
            return;
        }
        noGuardian.setVisibility(View.GONE);
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
        Toast.makeText(_mActivity, guardianError, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void commonClick(String info) {
        JDialogUtil.INSTANCE.showRxDialogSureCancel(getContext(), null, 0, "确认删除", new RxDialogSureCancelListener() {
            @Override
            public void RxDialogSure() {
                JDialogUtil.INSTANCE.dismiss();
                deleteFriend(Integer.parseInt(info));
            }

            @Override
            public void RxDialogCancel() {
                JDialogUtil.INSTANCE.dismiss();
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        friendsPresenter.reqFriendData(token);
    }

    private void deleteFriend(int id){
        JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
        String url = "friend/delete_friend";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("friendUserId", id)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                            friendsPresenter.reqFriendData(token);
                            JDialogUtil.INSTANCE.dismiss();
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            JDialogUtil.INSTANCE.dismiss();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }
}
