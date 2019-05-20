package com.yijia.common_yijia.main.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.common_tencent_tuikit.Constants;
import com.example.latte.ec.R;
import com.example.latte.ui.widget.RobotImageView;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.util.listener.OnSingleClickListener;
import com.yijia.common_yijia.main.find.healthself.HealthWaitDelegate;
import com.yijia.common_yijia.main.friends.SHImpl;
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{
                                Manifest.permission.RECORD_AUDIO}, 11);
                        return;
                    }
                }
                startCall("18911768630", "test", "");
            }
        });
    }

    private void startCall(String phone, String name, String url) {
        Intent intent = new Intent(getContext(), SHCallingActivity.class);
        intent.putExtra(ExtraString.ISINCOME, false);
        intent.putExtra(ExtraString.PHONE_NUM, phone);
        intent.putExtra(ExtraString.PHONE_NAME, name);
        intent.putExtra(ExtraString.PHONE_URL, url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                ((HealthMainDelegate)getParentDelegate()).loadFragment(new HealthWaitDelegate());
        }
    }


}
