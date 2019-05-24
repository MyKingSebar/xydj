package com.yijia.common_yijia.main.friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common_tencent_tuikit.Constants;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.ui.dialog.RxDialogSureCancelListener;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.adapter.MyFriendsAdapter;
import com.yijia.common_yijia.main.friends.bean.FriendsBean;
import com.yijia.common_yijia.main.friends.presenter.FriendsPresenter;
import com.yijia.common_yijia.main.friends.view.fragment.AddFriendsDelegate;
import com.yijia.common_yijia.main.friends.view.iview.FriendsView;
import com.yijia.common_yijia.main.index.InviteDelagate;
import com.yijia.common_yijia.main.index.invite.InviteRelationshipDelegate;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragment2;
import com.yijia.common_yijia.main.mine.UserDetailDelegate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;


/**
 * 亲友团列表
 */

public class FriendsDelegate2 extends LatteDelegate implements  FriendsView , CommonStringClickListener {
    @BindView(R2.id.rv)
    RecyclerView friendsRecycler;
    @BindView(R2.id.tv_save)
    AppCompatTextView tvSave;
    @BindView(R2.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R2.id.tv_icon)
    AppCompatTextView tvIcon;

    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }


    //亲友团好友列表
    private List<FriendsBean> friendsBeans;
    private FriendsPresenter friendsPresenter;

    PersonalChatFragment2 mCurrentFragment=null;
    String token=null;
    long familyId;
    @Override
    public Object setLayout() {
        return R.layout.delegate_friends2;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        familyId = getArguments().getLong("familyId");
        initView();
        friendsBeans = new ArrayList<>();
        friendsPresenter = new FriendsPresenter(this);
        token= YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
//        friendsPresenter.reqFriendData(token, familyId);
//        friendsPresenter.reqGuardianData(token);
    }

    private void initView() {
        tvTitle.setText("成员列表");
        tvSave.setVisibility(View.INVISIBLE);
        tvIcon.setVisibility(View.VISIBLE);
        tvIcon.setOnClickListener(v -> {
//            showIndexPopup(v);
//            getSupportDelegate().start(new AddFriendsDelegate());
            InviteRelationshipDelegate mDelegate = InviteRelationshipDelegate.create(familyId);
            getSupportDelegate().start(mDelegate);
        });
        tvIcon.setBackground(ContextCompat.getDrawable(getContext(),R.mipmap.icon_addfriend));
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
        //有好友的处理
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
            int isOnline = 2;
            if(jsonObject.containsKey("robotIsOnline"))
                isOnline = jsonObject.getInteger("robotIsOnline");
            String relation = "";
            if(jsonObject.containsKey("relationTypeName"))
                relation = jsonObject.getString("relationTypeName");
            FriendsBean friendsBean = new FriendsBean(friendUserId, nickname, realName, userStatus, userHead, identifier, isOnline, relation);
            friendsBeans.add(friendsBean);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) ;
        friendsRecycler.setLayoutManager(layoutManager);
        MyFriendsAdapter adapter = new MyFriendsAdapter(R.layout.friends_itme, friendsBeans);
        adapter.setmCommonClickListener(this);
        friendsRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {

            FriendsBean bean = friendsBeans.get(position);
            UserDetailDelegate detailDelegate = new UserDetailDelegate();
            Bundle bundle = getArguments();
            bundle.putLong("id", bean.getFriendUserId());
            bundle.putString("name", bean.getNickname());
            detailDelegate.setArguments(bundle);
            getSupportDelegate().start(detailDelegate);
//            String identifier = friendsBeans.get(position).getIdentifier();
//            String nickname = friendsBeans.get(position).getNickname();
//            String headUrl = friendsBeans.get(position).getUserHead();
//            Bundle mArgs = new Bundle();
//            mArgs.putString(Constants.INTENT_DATA, identifier);
//            mArgs.putString(Constants.INTENT_NAME, nickname);
//            mArgs.putString(Constants.INTENT_URL, headUrl);
//            mCurrentFragment = new PersonalChatFragment2();
//            mCurrentFragment.setArguments(mArgs);
//            getSupportDelegate().start(mCurrentFragment);

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
    }

    @Override
    public void respGuardianError(String guardianError) {
    }

    @Override
    public void commonClick(String info) {
        JDialogUtil.INSTANCE.showRxDialogSureCancel(getContext(), null, 0, "确认删除", new RxDialogSureCancelListener() {
            @Override
            public void RxDialogSure() {
                JDialogUtil.INSTANCE.dismiss();
                deleteFriend(Long.parseLong(info));
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
        friendsPresenter.reqFriendData(token, familyId);
    }

    private void deleteFriend(long id){
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
                            friendsPresenter.reqFriendData(token, familyId);
                            JDialogUtil.INSTANCE.dismiss();

                            final IGlobalCallback<Long> callback;
                            callback = CallbackManager
                                    .getInstance()
                                    .getCallback(CallbackType.REFRESH_FRIENDS_COUNT);
                            if (callback != null) {
                                callback.executeCallback(familyId);
                            }

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

    void showIndexPopup(View v) {
        QuickPopupBuilder.with(getContext())
                .contentView(R.layout.basepopu_addfriends)
                .config(new QuickPopupConfig()
                        .clipChildren(true)
//                        .backgroundColor(Color.parseColor("#8C617D8A"))
//                        .withShowAnimation(enterAnimation)
//                        .withDismissAnimation(dismissAnimation)
                        .gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .blurBackground(false, option -> option.setBlurRadius(6)
                                .setBlurPreScaleRatio(0.9f))
                        .withClick(R.id.tv_invite, v1 -> getSupportDelegate().start(new InviteDelagate()), true)
                        .withClick(R.id.tv_add, v1 -> getSupportDelegate().start(new AddFriendsDelegate()), true)
                )
                .show(v);
    }

}
