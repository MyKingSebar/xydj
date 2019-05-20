package com.yijia.common_yijia.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.common_tencent_tuikit.Constants;
import com.example.latte.ec.R;
import com.example.latte.ui.widget.RobotImageView;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.util.listener.OnSingleClickListener;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragment2;

public class UserDetailDelegate extends LatteDelegate {

    private RelativeLayout mBack;

    private AppCompatTextView mTitle;

    private RobotImageView robotImageView;

    private AppCompatTextView mName, mNickName, mRelationKey, mRelationValue, mPermissionValue, mSendMsg, mSendCall;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_user_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initView(rootView);
    }

    private void initView(View rootView) {
        mBack = rootView.findViewById(R.id.tv_back);
        mBack.setOnClickListener(v -> getSupportDelegate().pop());

        mTitle = rootView.findViewById(R.id.tv_title);
        mTitle.setText(R.string.user_detail_title);

        rootView.findViewById(R.id.tv_save).setVisibility(View.GONE);

        robotImageView = rootView.findViewById(R.id.user_detail_image);

        mName = rootView.findViewById(R.id.user_detail_name);

        mNickName = rootView.findViewById(R.id.user_detail_nickname);

        mRelationKey = rootView.findViewById(R.id.user_detail_relation_key);
        mRelationKey.setText(getString(R.string.user_detail_relation, getArguments().getString("familyName")));

        mRelationValue = rootView.findViewById(R.id.user_detail_relation_value);

        mPermissionValue = rootView.findViewById(R.id.user_detail_permission_value);

        mSendMsg = rootView.findViewById(R.id.user_detail_send_msg);
        mSendMsg.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View v) {
                long identifier = getArguments().getLong("id");
//                String nickname = getArguments().getString();
                Bundle mArgs = new Bundle();
                mArgs.putString(Constants.INTENT_DATA, identifier+"");
//                mArgs.putString(Constants.INTENT_NAME, nickname);
                PersonalChatFragment2 personalChatFragment2 = new PersonalChatFragment2();
                personalChatFragment2.setArguments(mArgs);
                getSupportDelegate().start(personalChatFragment2);
            }
        });

        mSendCall = rootView.findViewById(R.id.user_detail_send_call);
        mSendCall.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View v) {

            }
        });
    }

//    private void getInfo() {
////        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
////        long id = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getId();
//        RxRestClient.builder()
//                .url("user/query_user_info")
//                .params("yjtk", token)
//                .params("type", 1)
//                .params("keyword", id + "")
//                .build()
//                .get()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<String>(getContext()) {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("onResponse","user/query_user_info:"+response);
//                        final JSONObject obj = JSON.parseObject(response);
//                        final String status = obj.getString("status");
//                        if (TextUtils.equals(status, "1001")) {
//                            JSONObject data = obj.getJSONObject("data");
//                            JSONObject user = data.getJSONObject("user");
//                            String nickname = user.getString("nickname");
//                            String imagePath = user.getString("imagePath");
//                            long id = user.getLong("id");
//                            String phone = user.getString("phone");
//                            String email = user.getString("email");
//
//                            //用户状态：1-正常，2-注销
//                            int userStatus = user.getInteger("userStatus");
//                            String tencentImUserId = user.getString("tencentImUserId");
//                            String tencentImUserSig = user.getString("tencentImUserSig");
//                            String inviteCode = user.getString("inviteCode");
//
//                            if (nickname != null) {
//                                tv_name.setText(nickname);
//                            }
//                            if (imagePath != null) {
//                                GlideUtils.load(getContext(), imagePath, civ, GlideUtils.USERMODE);
//                            }
//
//                        } else {
//                            final String msg = JSON.parseObject(response).getString("msg");
//                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Throwable e) {
//                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

}
